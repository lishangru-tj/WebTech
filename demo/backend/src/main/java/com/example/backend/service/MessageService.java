package com.example.backend.service;

import com.example.backend.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/24
 * @JDKVersion 17.0.4
 */
@Service
public interface MessageService {
    List<Message> findActivityAllMessage(Long activityId);

    int addMessage(Message message);
}
