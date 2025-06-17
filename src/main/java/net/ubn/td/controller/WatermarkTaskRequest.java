package net.ubn.td.controller;

public class WatermarkTaskRequest {
    private String fileId;
    private String text;

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
