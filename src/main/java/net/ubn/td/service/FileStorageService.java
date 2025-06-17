package net.ubn.td.service;

import net.ubn.td.entity.FileRecord;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileRecord storeFile(MultipartFile file) throws IOException;
}
