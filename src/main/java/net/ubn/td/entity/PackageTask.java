package net.ubn.td.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class PackageTask {
    @Id
    private UUID id;
    private String fileId;
    private String packageType;
    private String status;
    private String errorMessage;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
