package com.example.backend.service.impl;

import com.example.backend.entity.UserNotice;
import com.example.backend.mapper.UserNoticeMapper;
import com.example.backend.service.UserNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Service
public class UserNoticeServiceImpl implements UserNoticeService {
    @Autowired
    UserNoticeMapper userNoticeMapper;

    @Override
    public List<UserNotice> findOneUserAllNotice(Long userId) {
        return userNoticeMapper.selectOneUserAllNotice(userId);
    }

    @Override
    public int modifyNoticeStatus(Long userId, Long noticeId) {
        return userNoticeMapper.updateNoticeStatus(userId, noticeId);
    }

    @Override
    public int addUserNotice(UserNotice userNotice) {
        return userNoticeMapper.insertUserNotice(userNotice);
    }

    @Override
    public Integer deleteUserNotice(Long user_id, Long notice_id) {
        Integer res=0;
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("notice_id",notice_id);
        res=userNoticeMapper.deleteByMap(map);

        return res;
    }
}
