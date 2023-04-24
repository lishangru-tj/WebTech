package com.example.backend.service;

import com.example.backend.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoteService {
    List<Long> findVote(Long userID);
    int insertOneVote(Long userID,Long option_id);
}
