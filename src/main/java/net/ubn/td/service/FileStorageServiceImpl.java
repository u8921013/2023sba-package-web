package net.ubn.td.service;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.repository.FileRecordRepository;
import net.ubn.td.util.UuidV7Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRecordRepository repository;

    public FileStorageServiceImpl(FileRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public FileRecord storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        String storedName = ext == null ? timestamp : timestamp + "." + ext;

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        Path target = dir.resolve(storedName);
        byte[] bytes = file.getBytes();
        Files.write(target, bytes);
        String fileId = DigestUtils.md5DigestAsHex(bytes);

        FileRecord record = new FileRecord();
        record.setId(UuidV7Generator.generate());
        record.setFileId(fileId);
        record.setDir(dir.toString());
        record.setStoredFilename(storedName);
        record.setOriginalFilename(originalFilename);

        repository.save(record);
        return record;
    }
}
