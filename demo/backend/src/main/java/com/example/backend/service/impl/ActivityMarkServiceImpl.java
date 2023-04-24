package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.ActivityMark;

import com.example.backend.mapper.ActivityMarkMapper;
import com.example.backend.service.ActivityMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/11:55 PM
 * @Description:
 */
@Service
public class ActivityMarkServiceImpl implements ActivityMarkService {
    @Autowired
    ActivityMarkMapper activityMarkMapper;

    @Override
    public Integer AddActivityMark(ActivityMark activityMark) {
        return activityMarkMapper.insert(activityMark);
    }

    @Override
    public List<Map<String,Object>>  GetActAllMark(Long activity_id) {
        List<Map<String,Object>>  activityMarkList=new ArrayList<>();
        activityMarkList=activityMarkMapper.SelectActMark(activity_id);

        return activityMarkList;
    }



    @Override
    public List<Map<String,Object>> GetUserMarktoAct(Long activity_id, Long user_id) {
        List<Map<String,Object>> list=new ArrayList<>();
        list=activityMarkMapper.SelectActMark(activity_id,user_id);
        return list;
    }
}
