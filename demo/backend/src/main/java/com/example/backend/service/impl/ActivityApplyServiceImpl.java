package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityApply;
import com.example.backend.mapper.ActivityApplyMapper;
import com.example.backend.service.ActivityApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 杨严
 * @Date: 2022/11/26/1:38 PM
 * @Description:
 */
@Service
public class ActivityApplyServiceImpl implements ActivityApplyService {
    @Autowired
    ActivityApplyMapper activityApplyMapper;


    @Override
    public ActivityUserRole isApplicant(Long user_id, Long activity_id) {
        ActivityUserRole role=new ActivityUserRole();
        role.setActivity_id(activity_id);
        role.setUser_id(user_id);

        QueryWrapper<ActivityApply> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("activity_id",activity_id)
                .eq("user_id",user_id);
        Integer count=activityApplyMapper.selectCount(queryWrapper);
        role.setIs_applicant(count==1?true:false);
        return role;
    }

    @Override
    public Integer UserApplyAct(ActivityApply activityApply) {
        Integer res=0;
//        System.out.println("activityApply"+activityApply);
        res=activityApplyMapper.insert(activityApply);
        return res;
    }

    @Override
    @Transactional
    public Integer UserCancelApplyAct(Long activity_id, Long user_id) {
        Integer res=0;
        Map<String,Object> map = new HashMap<>();
        map.put("activity_id",activity_id);
        map.put("user_id",user_id);
//        System.out.println(map);
//        System.out.println(activityApplyMapper.selectByMap(map));
        res=activityApplyMapper.deleteByMap(map);

        return res;
    }


    @Override
    public Integer CountApplicantNum(Long activity_id) {
        Integer num=0;
        QueryWrapper<ActivityApply> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("activity_id",activity_id);
        num=activityApplyMapper.selectCount(queryWrapper);

        return num;
    }

    @Override
    public Integer DeleteAct(Long activity_id) {
        Integer delete_num= 0;
        delete_num=activityApplyMapper.DeleteAct(activity_id);
        System.out.println("DeleteAct"+delete_num);
        return delete_num;
    }


    @Override
    public List<ActivityApply> GetAllApplicantList(Long activity_id) {
        List<Map<String,Object>> list_map=activityApplyMapper.SelectApplicantList(activity_id);
        List<ActivityApply> list=list_map
                .stream()
                .map(p->new ActivityApply((Long)p.get("activity_id"),(Long)p.get("user_id"),(Date) p.get("apply_time")))
                .collect(Collectors.toList());
        return list;
    }
}
