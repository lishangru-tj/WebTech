package com.example.backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private int age;
    private String introduce;
    private String avatar;
    private MultipartFile avatarFile;
}
