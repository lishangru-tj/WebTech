package com.example.backend.service;

import com.example.backend.entity.UserNotice;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Service
public interface UserNoticeService {
    List<UserNotice> findOneUserAllNotice(Long userId);

    int modifyNoticeStatus(Long userId, Long noticeId);

    int addUserNotice(UserNotice userNotice);

    Integer deleteUserNotice(Long user_id,Long notice_id);

}
