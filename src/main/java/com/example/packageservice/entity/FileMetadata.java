package com.example.packageservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileMetadata {
    @Id
    private UUID id;

    private String originalFilename;

    private String storedPath;

    private Instant createdAt;

    public FileMetadata() {
    }

    public FileMetadata(UUID id, String originalFilename, String storedPath, Instant createdAt) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedPath = storedPath;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredPath() {
        return storedPath;
    }

    public void setStoredPath(String storedPath) {
        this.storedPath = storedPath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
