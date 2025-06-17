package net.ubn.td.controller;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.exceptions.ApiReturn;
import net.ubn.td.exceptions.ResponseCode;
import net.ubn.td.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileStorageService storageService;

    public FileUploadController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiReturn<FileUploadResponse>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        FileRecord record = storageService.storeFile(file);
        FileUploadResponse res = new FileUploadResponse(record.getFileId(), record.getStoredFilename());

        ApiReturn<FileUploadResponse> api = new ApiReturn<>();
        api.setCode(ResponseCode.M000);
        api.setMessage("success");
        api.setData(res);
        return ResponseEntity.ok(api);
    }
}
