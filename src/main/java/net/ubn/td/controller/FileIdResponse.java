package net.ubn.td.controller;

public class FileIdResponse {
    private String fileId;

    public FileIdResponse() {}

    public FileIdResponse(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
