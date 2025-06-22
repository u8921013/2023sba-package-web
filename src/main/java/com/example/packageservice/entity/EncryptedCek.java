package com.example.packageservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "encrypted_cek")
public class EncryptedCek {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "file_id")
    private FileMetadata file;

    private String encryptedKey;

    private Instant createdAt;

    public EncryptedCek() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FileMetadata getFile() {
        return file;
    }

    public void setFile(FileMetadata file) {
        this.file = file;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
