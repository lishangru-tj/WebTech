package com.example.backend.dto;

import lombok.Data;

@Data
public class UserForgetRequestDTO {
    private String id;
    private String code;
    private String pwd;
}
