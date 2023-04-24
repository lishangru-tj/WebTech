package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.Classification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassificationService {
    List<Classification> findOneUserAllClassificationTitle(Long userId);
    int insertOneClassification(Long userId, String classificationTitle);
    int deleteOneClassification(Long userId, String classificationTitle);
}
