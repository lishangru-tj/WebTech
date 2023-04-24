package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.dto.UserForgetRequestDTO;
import com.example.backend.entity.UserForgetRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserForgetRequestService {
    Result<UserForgetRequest> forgetRequest(UserForgetRequest userForgetRequest);
    Result<String> resetPwd(UserForgetRequestDTO userForgetRequest);
}
