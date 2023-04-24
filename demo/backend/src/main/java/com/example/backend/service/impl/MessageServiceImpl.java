package com.example.backend.service.impl;

import com.example.backend.entity.Message;
import com.example.backend.mapper.MessageMapper;
import com.example.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/24
 * @JDKVersion 17.0.4
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Message> findActivityAllMessage(Long activityId) {
        return messageMapper.selectActivityAllMessage(activityId);
    }

    @Override
    public int addMessage(Message message) {
        return messageMapper.insertMessage(message);
    }
}
