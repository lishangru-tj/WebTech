package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.DustbinTask;
import com.example.backend.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DustbinService {
    Result<List<DustbinTask>> findAllDustbinTask(Long userId);
    Result<String> smashOneRubbish(Long id);
    Result<String> clearDustbin(Long userId);
    Result<String> restoreOneRubbish(Long id);
}
