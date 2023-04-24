package com.example.backend.service.impl;

import org.apache.commons.collections.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.ActivityTag;
import com.example.backend.entity.Tag;
import com.example.backend.mapper.ActivityTagMapper;
import com.example.backend.service.ActivityTagService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/01/7:21 PM
 * @Description:
 */
@Service
public class ActivityTagServiceImpl implements ActivityTagService {
    @Autowired
    ActivityTagMapper activityTagMapper;
    @Override
    public Integer AddActTags(Long activity_id, List<Long> tagList) {
        for (Long tag_id:tagList){
            activityTagMapper.insert(new ActivityTag(activity_id,tag_id));
        }
        return null;
    }

    @Override
    public Integer DeleteActAllTags(Long activity_id) {
        return activityTagMapper.DeleteActAllTags(activity_id);
    }

    @Override
    public List<Map<String, Object>> GetActAllTags(Long activity_id) {
        return activityTagMapper.SelectActAllTags(activity_id);
    }

    public List<ActivityTag> getActTags(Long activity_id){
        QueryWrapper<ActivityTag> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("activity_id",activity_id);
        List<ActivityTag> list=activityTagMapper.selectList(queryWrapper);
        System.out.println(activityTagMapper.selectList(queryWrapper));

        return list;
    }
    public List<Long> getActIDByTags(List<Long> requiredTags) {
        List<List<ActivityTag>> activityGroups= new ArrayList<>();
        for(Long tag_id:requiredTags)
        {
            QueryWrapper<ActivityTag> queryWrapper=new QueryWrapper<>();
            queryWrapper
                    .eq("tag_id",tag_id);
            List<ActivityTag> activities=activityTagMapper.selectList(queryWrapper);
            activityGroups.add(activities);
        }
        List<Long> result=new ArrayList<>();
        for(ActivityTag activityTag:activityGroups.get(0))
            result.add(activityTag.getActivity_id());
        int i=1;
        for(;i<activityGroups.size();i++)
        {
            List<Long> list=new ArrayList<>();
            for(ActivityTag activityTag:activityGroups.get(i))
                list.add(activityTag.getActivity_id());
            result = Lists.newArrayList(CollectionUtils.intersection(result, list));
        }
        return result;
    }
    public List<Long> getActIDByTag(Long requiredTagID) {
        QueryWrapper<ActivityTag> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("tag_id",requiredTagID);
        List<ActivityTag> activities=activityTagMapper.selectList(queryWrapper);
        List<Long> result=new ArrayList<>();
        for(ActivityTag activityTag:activities)
            result.add(activityTag.getActivity_id());
        return result;
    }
}
