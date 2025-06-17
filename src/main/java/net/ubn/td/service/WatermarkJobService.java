package net.ubn.td.service;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.entity.WatermarkTask;
import net.ubn.td.repository.FileRecordRepository;
import net.ubn.td.repository.WatermarkTaskRepository;
import net.ubn.td.util.UuidV7Generator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WatermarkJobService {

    private final WatermarkTaskRepository repository;
    private final FileRecordRepository fileRepository;
    private final WatermarkProcessor processor;
    private static final int MAX_RETRIES = 3;

    public WatermarkJobService(WatermarkTaskRepository repository,
                               FileRecordRepository fileRepository,
                               WatermarkProcessor processor) {
        this.repository = repository;
        this.fileRepository = fileRepository;
        this.processor = processor;
    }

    @Scheduled(fixedDelay = 60000)
    public void scanTasks() {
        List<WatermarkTask> tasks = repository.findByStatus("initial");
        tasks.forEach(this::processTask);
    }

    @Scheduled(fixedDelay = 300000)
    public void scanProcessingTasks() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        List<WatermarkTask> tasks = repository.findByStatusAndModificationDateBefore("processing", threshold);
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

    void processTask(WatermarkTask task) {
        try {
            task.setStatus("processing");
            task.setModificationDate(LocalDateTime.now());
            repository.save(task);

            FileRecord record = fileRepository.findByFileId(task.getFileId())
                    .orElseThrow(() -> new RuntimeException("file not found"));
            Path input = Paths.get(record.getDir()).resolve(record.getStoredFilename());
            Path output = processor.applyWatermark(input, task.getText());
            byte[] bytes = Files.readAllBytes(output);

            FileRecord outRec = new FileRecord();
            outRec.setId(UuidV7Generator.generate());
            outRec.setFileId(DigestUtils.md5DigestAsHex(bytes));
            outRec.setDir(output.getParent().toString());
            outRec.setStoredFilename(output.getFileName().toString());
            outRec.setOriginalFilename(record.getOriginalFilename());
            fileRepository.save(outRec);

            task.setOutputFileId(outRec.getFileId());
            task.setStatus("done");
        } catch (Exception e) {
            task.setStatus("failed");
            task.setErrorMessage(e.getMessage());
        }
        task.setModificationDate(LocalDateTime.now());
        repository.save(task);
    }
}
