package com.example.backend.service;

import com.example.backend.entity.File;
import com.example.backend.entity.Notice;
import org.springframework.stereotype.Service;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/3
 * @JDKVersion 17.0.4
 */
@Service
public interface FileService {
    File getFileInfoById(Long fileId);
}
