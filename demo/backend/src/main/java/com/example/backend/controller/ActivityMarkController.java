package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.ActivityMark;
import com.example.backend.service.ActivityMarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/11:58 PM
 * @Description:
 */
@Api(tags = {"ActivityMark"})
@RestController
@RequestMapping("activitymark")
public class ActivityMarkController {
    @Autowired
    ActivityMarkService activityMarkService;

    @ApiOperation("新增一条活动评价")
    @PostMapping("postActMark")
    public Result<Map<String,Object>> AddOneActMark(@RequestBody ActivityMark activityMark){
        Map<String,Object> map=new HashMap<>();
        map.put("activityMark",activityMark);
        try{
            int a=0;
            System.out.println(a/2);
            activityMarkService.AddActivityMark(activityMark);
            map.put("msg","SUCCESS");
        }catch (Exception e){
            return  Result.fail(HttpStatus.EXPECTATION_FAILED.value(),"AddOneActMark FAILED");
        }
        return Result.success(map);
    }


//    /**
//     * 查询用户某个活动的评价，若无则返回空
//     */
//    @ApiOperation("查询用户某个活动的评价，若无则返回空")
//    @GetMapping("/getUserMarkToAct")
//    public Result<Map<String,Object>> GetUserMarkToAct(
//            @ApiParam(name = "id", value = "活动id", required = true)
//            @RequestParam("activity_id") Long activity_id,
//            @ApiParam(name = "id", value = "用户id", required = true)
//            @RequestParam("user_id") Long user_id) {
//        try {
//        } catch (Exception e) {
//            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "GetUserMarkToAct FAILED");
//        }
//    }

}
