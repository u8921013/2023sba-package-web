package net.ubn.td.controller;

import net.ubn.td.exceptions.ApiReturn;
import net.ubn.td.exceptions.ResponseCode;
import net.ubn.td.service.FileRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileQueryController {

    private final FileRecordService recordService;

    public FileQueryController(FileRecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/lookup")
    public ResponseEntity<ApiReturn<FileIdResponse>> getFileId(@RequestParam("path") String path) {
        String fileId = recordService.getFileIdByPath(path);
        if (fileId == null) {
            return ResponseEntity.notFound().build();
        }
        ApiReturn<FileIdResponse> api = new ApiReturn<>();
        api.setCode(ResponseCode.M000);
        api.setMessage("success");
        api.setData(new FileIdResponse(fileId));
        return ResponseEntity.ok(api);
    }
}
