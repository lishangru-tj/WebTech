package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.DustbinTask;
import com.example.backend.service.DustbinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Api(tags = {"Dustbin"})
@RestController
@RequestMapping("dustbin")
public class DustbinController {
    @Autowired
    DustbinService dustbinService;

    @ApiOperation("返回该用户所有垃圾桶里的事项")
    @GetMapping("findAllRubbish")
    public Result<List<DustbinTask>> findAllRubbish(@ApiParam(name="userId", value="用户id", required = true)
                                                        @RequestParam("userId") Long userId){
        return dustbinService.findAllDustbinTask(userId);
    }

    @ApiOperation("根据taskId删除垃圾")
    @DeleteMapping("smashOneRubbish")
    public Result<String> smashOneRubbish(@ApiParam(name="id", value="要永久删除的事项id", required = true)
                                              @RequestParam("id") Long id)
    {
        return dustbinService.smashOneRubbish(id);
    }

    @ApiOperation("清空该用户的回收站")
    @DeleteMapping("clearDustbin")
    public Result<String> smashAllRubbish(@ApiParam(name="userId", value="用户id", required = true)
                                              @RequestParam("userId") Long userId)
    {
        return dustbinService.clearDustbin(userId);
    }

    @ApiOperation("根据taskId恢复事项")
    @PatchMapping("restoreOneRubbish")
    public Result<String> restoreOneRubbish(@ApiParam(name="id", value="要恢复的事项id", required = true)
                                                @RequestParam("id") Long id)
    {
        return dustbinService.restoreOneRubbish(id);
    }
}
