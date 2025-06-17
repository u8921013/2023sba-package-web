package net.ubn.td.controller;

import net.ubn.td.exceptions.ApiReturn;
import net.ubn.td.exceptions.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {


    @GetMapping("/sayHello")
    public ResponseEntity<ApiReturn<String>> sayHello() {
        ApiReturn<String> apiReturn=new ApiReturn<>();
        apiReturn.setCode(ResponseCode.M000);
        apiReturn.setMessage("success");
        apiReturn.setData("Hello");
        return ResponseEntity.ok(apiReturn);
    }
}
