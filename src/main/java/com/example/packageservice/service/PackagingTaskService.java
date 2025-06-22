package com.example.packageservice.service;

import com.example.packageservice.config.PackagingProperties;
import com.example.packageservice.entity.FileMetadata;
import com.example.packageservice.entity.PackagingTask;
import com.example.packageservice.entity.TaskStatus;
import com.example.packageservice.entity.TaskType;
import com.example.packageservice.repository.FileMetadataRepository;
import com.example.packageservice.repository.PackagingTaskRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PackagingTaskService {
    private final PackagingTaskRepository taskRepo;
    private final FileMetadataRepository fileRepo;
    private final PackagingProperties properties;
    private final OAuth2AuthorizedClientManager clientManager;
    private final RestTemplate restTemplate = new RestTemplate();

    public PackagingTaskService(PackagingTaskRepository taskRepo,
                                FileMetadataRepository fileRepo,
                                PackagingProperties properties,
                                OAuth2AuthorizedClientManager clientManager) {
        this.taskRepo = taskRepo;
        this.fileRepo = fileRepo;
        this.properties = properties;
        this.clientManager = clientManager;
    }

    public PackagingTask createTask(UUID fileId, TaskType type) {
        FileMetadata file = fileRepo.findById(fileId).orElseThrow();
        PackagingTask task = new PackagingTask();
        task.setId(UuidCreator.getTimeOrderedEpoch());
        task.setFile(file);
        task.setType(type);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(Instant.now());
        return taskRepo.save(task);
    }

    public Optional<PackagingTask> getTask(UUID id) {
        return taskRepo.findById(id);
    }

    /**
     * Process up to N pending tasks.
     */
    @Transactional
    public void processPendingTasks() {
        List<PackagingTask> tasks = taskRepo.findByStatusOrderByCreatedAtAsc(
                TaskStatus.PENDING, PageRequest.of(0, properties.getBatchSize()));
        for (PackagingTask task : tasks) {
            task.setStatus(TaskStatus.PROCESSING);
            task.setStartedAt(Instant.now());
            taskRepo.save(task);
            try {
                // TODO real packaging logic according to task.getType()
                File source = new File(task.getFile().getStoredPath());
                File dest = new File(source.getParent(), source.getName() + ".pkg");
                dest.createNewFile();
                task.setPackagedPath(dest.getAbsolutePath());
                // send metadata after packaging
                callRemoteApi(task);
                task.setStatus(TaskStatus.COMPLETED);
                task.setFinishedAt(Instant.now());
            } catch (Exception e) {
                task.setStatus(TaskStatus.FAILED);
            }
            taskRepo.save(task);
        }
    }

    /**
     * Mark tasks running longer than configured timeout.
     */
    @Transactional
    public void cleanupTimeoutTasks() {
        Instant cutoff = Instant.now().minusSeconds(properties.getTimeoutMinutes() * 60);
        List<PackagingTask> list = taskRepo.findByStatusAndStartedAtBefore(TaskStatus.PROCESSING, cutoff);
        for (PackagingTask task : list) {
            task.setStatus(TaskStatus.TIMEOUT);
            taskRepo.save(task);
        }
    }

    private void callRemoteApi(PackagingTask task) {
        OAuth2AuthorizeRequest req = OAuth2AuthorizeRequest.withClientRegistrationId("remote")
                .principal("package-service")
                .build();
        OAuth2AuthorizedClient client = clientManager.authorize(req);
        if (client == null) {
            throw new IllegalStateException("Unable to authorize client");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(client.getAccessToken().getTokenValue());
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<String> request = RequestEntity.post(properties.getRemoteApiUrl())
                .headers(headers)
                .body("{\"taskId\":\"" + task.getId() + "\"}");
        restTemplate.exchange(request, String.class);
    }
}
