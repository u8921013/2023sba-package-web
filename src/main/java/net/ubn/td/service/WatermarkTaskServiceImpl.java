package net.ubn.td.service;

import net.ubn.td.entity.WatermarkTask;
import net.ubn.td.repository.WatermarkTaskRepository;
import net.ubn.td.util.UuidV7Generator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WatermarkTaskServiceImpl implements WatermarkTaskService {

    private final WatermarkTaskRepository repository;

    public WatermarkTaskServiceImpl(WatermarkTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createTask(String fileId, String text) {
        WatermarkTask task = new WatermarkTask();
        task.setId(UuidV7Generator.generate());
        task.setFileId(fileId);
        task.setText(text);
        task.setStatus("initial");
        task.setRetryCount(0);
        task.setModificationDate(LocalDateTime.now());
        repository.save(task);
        return task.getId().toString();
    }
}
