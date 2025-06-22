package com.example.packageservice.service;

import com.example.packageservice.config.PackagingProperties;
import com.example.packageservice.entity.EncryptedCek;
import com.example.packageservice.entity.FileMetadata;
import com.example.packageservice.repository.EncryptedCekRepository;
import com.example.packageservice.repository.FileMetadataRepository;
import com.example.packageservice.util.CekUtil;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class FileService {

    private final FileMetadataRepository fileRepo;
    private final EncryptedCekRepository cekRepo;
    private final PackagingProperties properties;

    public FileService(FileMetadataRepository fileRepo, EncryptedCekRepository cekRepo, PackagingProperties properties) {
        this.fileRepo = fileRepo;
        this.cekRepo = cekRepo;
        this.properties = properties;
    }

    public FileMetadata storeFile(MultipartFile multipartFile) throws IOException {
        UUID id = UuidCreator.getTimeOrderedEpoch(); // UUIDv7-like
        File dir = new File(properties.getStoragePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, id.toString() + "-" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(dest);

        FileMetadata meta = new FileMetadata(id, multipartFile.getOriginalFilename(), dest.getAbsolutePath(), Instant.now());
        fileRepo.save(meta);

        // Generate CEK and store encrypted version
        String encrypted = CekUtil.generateAndEncryptCek(properties.getMasterKey());
        EncryptedCek cek = new EncryptedCek();
        cek.setId(UuidCreator.getTimeOrderedEpoch());
        cek.setFile(meta);
        cek.setEncryptedKey(encrypted);
        cek.setCreatedAt(Instant.now());
        cekRepo.save(cek);

        return meta;
    }
}
