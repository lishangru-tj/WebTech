package com.example.backend.service.impl;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Activity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.ActivityApply;
import com.example.backend.entity.ActivityParticipate;
import com.example.backend.mapper.ActivityParticipateMapper;
import com.example.backend.service.ActivityApplyService;
import com.example.backend.service.ActivityParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/9:02 AM
 * @Description:
 */
@Service
public class ActivityParticipateServiceImpl implements ActivityParticipateService {
    @Autowired
    ActivityParticipateMapper activityParticipateMapper;

    @Override
    public Integer AddParticipant(ActivityParticipate participate) {
        Integer newID=activityParticipateMapper.insert(participate);
        return newID;
    }

    @Override
    public Integer RemoveParticipant(ActivityParticipate participate) {
        Integer res;
        QueryWrapper<ActivityParticipate> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("activity_id",participate.getActivity_id())
                .eq("user_id",participate.getUser_id());
        res=activityParticipateMapper.delete(queryWrapper);
        return res;
    }

    @Override
    public Integer CountParticipantNum(Long activity_id) {
        Integer num=0;
        QueryWrapper<ActivityParticipate> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("activity_id",activity_id);
        num=activityParticipateMapper.selectCount(queryWrapper);

        return num;
    }

    @Override
    public Integer DeleteAct(Long activity_id) {
        Integer delete_num= 0;
        delete_num=activityParticipateMapper.DeleteAct(activity_id);;
        return delete_num;
    }

    @Override
    public List<Map<String, Object>> SelectActApplicantList(Long activity_id) {
        List<Map<String,Object>> list=new ArrayList<>();
        list=activityParticipateMapper.SelectActApplicantList(activity_id);
        return list;
    }
    @Override
    public ActivityUserRole isMember(Long user_id, Long activity_id) {
        ActivityUserRole role=new ActivityUserRole();
        role.setActivity_id(activity_id);
        role.setUser_id(user_id);

        QueryWrapper<ActivityParticipate> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("user_id",user_id)
                .eq("activity_id",activity_id);
        Integer count=activityParticipateMapper.selectCount(queryWrapper);
        role.setIs_member(count==1?true:false);
        return role;

    }

    @Override
    public List<Long> FindOneUserAllActivityId(Long user_id) {
        return activityParticipateMapper.SelectOneUserAllActivityId(user_id);
    }

}
