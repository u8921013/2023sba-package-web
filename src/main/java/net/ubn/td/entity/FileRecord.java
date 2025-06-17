package net.ubn.td.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class FileRecord {
    @Id
    private UUID id;
    private String fileId;
    private String dir;
    private String storedFilename;
    private String originalFilename;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }
    public String getStoredFilename() { return storedFilename; }
    public void setStoredFilename(String storedFilename) { this.storedFilename = storedFilename; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
}
