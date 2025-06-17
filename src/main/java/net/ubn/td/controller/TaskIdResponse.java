package net.ubn.td.controller;

public class TaskIdResponse {
    private String taskId;

    public TaskIdResponse() {}
    public TaskIdResponse(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
}
