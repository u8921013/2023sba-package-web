package com.example.packageservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PackagingScheduler {
    private final PackagingTaskService taskService;

    public PackagingScheduler(PackagingTaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedDelayString = "PT1M")
    public void runTasks() {
        taskService.processPendingTasks();
        taskService.cleanupTimeoutTasks();
    }
}
