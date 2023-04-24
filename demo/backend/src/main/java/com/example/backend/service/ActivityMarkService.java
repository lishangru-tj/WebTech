package com.example.backend.service;

import com.example.backend.entity.ActivityMark;

import com.example.backend.mapper.ActivityMarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/11:53 PM
 * @Description:
 */
@Service
public interface ActivityMarkService {

    /**
     * 添加 一条活动评价
     */
    Integer AddActivityMark(ActivityMark activityMark);

    /**
     * 获取某活动的所有评价
     */
    List<Map<String,Object>>  GetActAllMark(Long activity_id);


    /**查询用户某个活动的评价，若无则返回空*/
    List<Map<String,Object>> GetUserMarktoAct(Long activity_id, Long user_id);
}
