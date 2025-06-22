package com.example.packageservice.controller;

import com.example.packageservice.entity.FileMetadata;
import com.example.packageservice.entity.PackagingTask;
import com.example.packageservice.entity.TaskType;
import com.example.packageservice.service.FileService;
import com.example.packageservice.service.PackagingTaskService;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;
    private final PackagingTaskService taskService;

    public FileController(FileService fileService, PackagingTaskService taskService) {
        this.fileService = fileService;
        this.taskService = taskService;
    }

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        FileMetadata meta = fileService.storeFile(file);
        return ResponseEntity.ok(meta.getId().toString());
    }

    @PostMapping("/package")
    public ResponseEntity<String> requestPackage(@RequestParam UUID fileId,
                                                 @RequestParam TaskType type) {
        PackagingTask task = taskService.createTask(fileId, type);
        return ResponseEntity.ok(task.getId().toString());
    }

    @GetMapping("/package/{id}")
    public ResponseEntity<String> getStatus(@PathVariable UUID id) {
        return taskService.getTask(id)
                .map(t -> ResponseEntity.ok(t.getStatus().name()))
                .orElse(ResponseEntity.notFound().build());
    }
}
