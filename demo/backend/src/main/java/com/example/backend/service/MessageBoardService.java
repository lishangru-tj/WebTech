package com.example.backend.service;

import com.example.backend.entity.MessageBoard;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/6
 * @JDKVersion 17.0.4
 */
@Service
public interface MessageBoardService {
    List<MessageBoard> findActivityAllMessageBoard(Long activityId);

    int addMessageBoard(MessageBoard messageBoard);

    Integer deleteMessageBoard(Long message_board_id);

}
