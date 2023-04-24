package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.UserForgetRequestDTO;
import com.example.backend.entity.UserForgetRequest;
import com.example.backend.service.UserForgetRequestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"User"})
@RestController
@RequestMapping("user")

public class UserForgetRequestController {
    @Autowired
    private UserForgetRequestService userForgetRequestService;
    @PostMapping("forget")
    public Result<UserForgetRequest> forget(@RequestBody UserForgetRequest userForgetRequest)
    {
//        System.out.println("@PostMapping(\"forget\")");
        return userForgetRequestService.forgetRequest(userForgetRequest);
    }
    @PutMapping("forget")
    public Result<String> reset(@RequestBody UserForgetRequestDTO userForgetRequest){
        return userForgetRequestService.resetPwd(userForgetRequest);
    }

}
