package net.ubn.td.service;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.repository.FileRecordRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileRecordServiceImpl implements FileRecordService {

    private final FileRecordRepository repository;

    public FileRecordServiceImpl(FileRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getFileIdByPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Path p = Paths.get(path);
        String filename = p.getFileName().toString();
        String dir = p.getParent() == null ? "" : p.getParent().toString();
        return repository.findByDirAndStoredFilename(dir, filename)
                .map(FileRecord::getFileId)
                .orElse(null);
    }
}
