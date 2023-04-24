package com.example.backend.service.impl;

import com.example.backend.entity.MessageBoard;
import com.example.backend.mapper.MessageBoardMapper;
import com.example.backend.service.MessageBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/6
 * @JDKVersion 17.0.4
 */
@Service
public class MessageBoardServiceImpl implements MessageBoardService {
    @Autowired
    MessageBoardMapper messageBoardMapper;

    @Override
    public List<MessageBoard> findActivityAllMessageBoard(Long activityId) {
        return messageBoardMapper.selectActivityAllMessageBoard(activityId);
    }

    @Override
    public int addMessageBoard(MessageBoard messageBoard) {
        return messageBoardMapper.insertMessageBoard(messageBoard);
    }

    @Override
    public Integer deleteMessageBoard(Long message_board_id) {
        Integer i=messageBoardMapper.deleteById(message_board_id);

        return i;
    }
}
