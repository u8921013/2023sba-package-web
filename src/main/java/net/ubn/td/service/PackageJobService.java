package net.ubn.td.service;

import net.ubn.td.entity.PackageTask;
import net.ubn.td.repository.PackageTaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PackageJobService {

    private final PackageTaskRepository repository;
    private static final int MAX_RETRIES = 3;

    public PackageJobService(PackageTaskRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 60000)
    public void scanTasks() {
        List<PackageTask> tasks = repository.findByStatus("initial");
        tasks.forEach(this::processTask);
    }

    @Scheduled(fixedDelay = 300000)
    public void scanProcessingTasks() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        List<PackageTask> tasks = repository.findByStatusAndModificationDateBefore("processing", threshold);
        tasks.forEach(task -> {
            if (task.getRetryCount() < MAX_RETRIES) {
                task.setRetryCount(task.getRetryCount() + 1);
                processTask(task);
            } else {
                task.setStatus("failed");
                repository.save(task);
            }
        });
    }

    void processTask(PackageTask task) {
        try {
            task.setStatus("processing");
            task.setModificationDate(LocalDateTime.now());
            repository.save(task);
            if ("LCP".equalsIgnoreCase(task.getPackageType())) {
                doLcpPackage(task);
            } else {
                doHlsPackage(task);
            }
            task.setStatus("done");
        } catch (Exception e) {
            task.setStatus("failed");
            task.setErrorMessage(e.getMessage());
        }
        task.setModificationDate(LocalDateTime.now());
        repository.save(task);
    }

    protected void doLcpPackage(PackageTask task) {
        // placeholder for real LCP packaging logic
    }

    protected void doHlsPackage(PackageTask task) {
        // placeholder for real HLS packaging logic
    }
}
