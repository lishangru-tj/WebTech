package com.example.backend.service;

import org.springframework.stereotype.Service;

@Service
public interface RelativeService {
    int insertOneRelative(Long taskId, Long relativeTaskId);
    int deleteOneTaskRelative(Long taskId);
}
