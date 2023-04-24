package com.example.backend.service;

import com.example.backend.entity.ActivityTag;
import com.example.backend.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/01/7:18 PM
 * @Description:
 */
@Service
public interface ActivityTagService {

    /**为活动新增tags,输入id的列表*/
    Integer AddActTags(Long activity_id, List<Long>tagList);

    /**删除活动的所有tag*/
    Integer DeleteActAllTags(Long activity_id);

    /**获取活动的所有tag*/
    List<Map<String, Object>> GetActAllTags(Long activity_id);

    List<ActivityTag> getActTags(Long activity_id);
    List<Long> getActIDByTags(List<Long> requiredTags);
    List<Long> getActIDByTag(Long requiredTagID);


}
