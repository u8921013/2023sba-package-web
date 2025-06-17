package net.ubn.td.controller;

import net.ubn.td.exceptions.ApiReturn;
import net.ubn.td.exceptions.ResponseCode;
import net.ubn.td.service.PackageTaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/package")
public class PackageTaskController {

    private final PackageTaskService taskService;

    public PackageTaskController(PackageTaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiReturn<TaskIdResponse>> createTask(@RequestBody PackageTaskRequest request) {
        String id = taskService.createTask(request.getFileId(), request.getPackageType());
        ApiReturn<TaskIdResponse> api = new ApiReturn<>();
        api.setCode(ResponseCode.M000);
        api.setMessage("success");
        api.setData(new TaskIdResponse(id));
        return ResponseEntity.ok(api);
    }
}
