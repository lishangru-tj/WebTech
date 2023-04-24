package com.example.backend.service.impl;

import com.example.backend.entity.File;
import com.example.backend.mapper.FileMapper;
import com.example.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/3
 * @JDKVersion 17.0.4
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Override
    public File getFileInfoById(Long fileId) {
        return fileMapper.selectFileInfo(fileId);
    }
}
