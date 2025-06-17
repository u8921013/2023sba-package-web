package net.ubn.td.controller;

public class FileUploadResponse {
    private String fileId;
    private String storedFilename;

    public FileUploadResponse() {}

    public FileUploadResponse(String fileId, String storedFilename) {
        this.fileId = fileId;
        this.storedFilename = storedFilename;
    }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getStoredFilename() { return storedFilename; }
    public void setStoredFilename(String storedFilename) { this.storedFilename = storedFilename; }
}
