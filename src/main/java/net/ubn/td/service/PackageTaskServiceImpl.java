package net.ubn.td.service;

import net.ubn.td.entity.PackageTask;
import net.ubn.td.repository.PackageTaskRepository;
import net.ubn.td.util.UuidV7Generator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PackageTaskServiceImpl implements PackageTaskService {

    private final PackageTaskRepository repository;

    public PackageTaskServiceImpl(PackageTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createTask(String fileId, String packageType) {
        PackageTask task = new PackageTask();
        task.setId(UuidV7Generator.generate());
        task.setFileId(fileId);
        task.setPackageType(packageType);
        task.setStatus("initial");
        task.setRetryCount(0);
        task.setModificationDate(LocalDateTime.now());
        repository.save(task);
        return task.getId().toString();
    }
}
