package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Tag;
import com.example.backend.service.ActivityTagService;
import com.example.backend.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/01/7:31 PM
 * @Description:
 */
@Api(tags = {"Tag"})
@RestController
@RequestMapping("tag")
public class TagController {
    @Autowired
    TagService tagService;
    @Autowired
    ActivityTagService activityTagService;

    @ApiOperation("获取所有tag")
    @GetMapping("/getAllTags")
    public Result<List<Map<String,Object>>> GetAllTags() {
        try {
            List<Map<String,Object>> tagList = tagService.GetAllTags();
            System.out.println("123tagList:"+tagList);
            return Result.success(tagList);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAllTags FAILED");
        }

    }




}
