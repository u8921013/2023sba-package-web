package net.ubn.td.controller;

import net.ubn.td.exceptions.ApiReturn;
import net.ubn.td.exceptions.ResponseCode;
import net.ubn.td.service.WatermarkTaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/watermark")
public class WatermarkTaskController {

    private final WatermarkTaskService service;

    public WatermarkTaskController(WatermarkTaskService service) {
        this.service = service;
    }

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiReturn<TaskIdResponse>> createTask(@RequestBody WatermarkTaskRequest request) {
        String id = service.createTask(request.getFileId(), request.getText());
        ApiReturn<TaskIdResponse> api = new ApiReturn<>();
        api.setCode(ResponseCode.M000);
        api.setMessage("success");
        api.setData(new TaskIdResponse(id));
        return ResponseEntity.ok(api);
    }
}
