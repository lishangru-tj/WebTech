package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityTag;
import com.example.backend.entity.Tag;
import com.example.backend.service.ActivityService;
import com.example.backend.service.ActivityTagService;
import com.example.backend.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"ActivityTag"})
@RestController
@RequestMapping("activityTag")
public class ActivityTagController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityTagService activityTagService;
    @Autowired
    private TagService tagService;

    @ApiOperation("获取活动标签")
    @GetMapping("/getActTag/{activity_id}")
    public Result<List<Tag>> findActTags(@ApiParam(name = "activity_id", value = "actid", required = true)
                                                   @PathVariable("activity_id") Long activity_id) {
        List<ActivityTag> activityTagList;
        List<Tag> tagList=new ArrayList<>();
        try {
            activityTagList = activityTagService.getActTags(activity_id);

            if(activityTagList.size()==0)
                return Result.success(tagList);
            for(ActivityTag activityTag :activityTagList)
            {
                System.out.println(activityTag.getTag_id());
                tagList.add(tagService.findTag(activityTag.getTag_id()));
            }
            return Result.success(tagList);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getActTag failed");
        }
    }
    @ApiOperation("根据标签筛选活动")
    @PostMapping("/getActByTag")
    public Result<List<Activity>> findActTags(@RequestBody List<Long> requiredTags) {
        List<Long> activityIDList=new ArrayList<>();
        List<Activity> activityList=new ArrayList<>();
        try {
            if (requiredTags.size() > 1)
                activityIDList = activityTagService.getActIDByTags(requiredTags);
            else if (requiredTags.size() == 1)
                activityIDList = activityTagService.getActIDByTag(requiredTags.get(0));
            for (Long act_id : activityIDList) {
                activityList.add(activityService.getAct(act_id));
            }
            return Result.success(activityList);
        }
        catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getActTag failed");
        }
    }
}

