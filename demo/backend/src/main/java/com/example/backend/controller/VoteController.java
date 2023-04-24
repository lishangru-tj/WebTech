package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Vote"})
@RestController
@RequestMapping("vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @ApiOperation("获取用户已经投过的选项")
    @GetMapping("/getvote/{userID}")
    public Result<List<Long>> findVote(@ApiParam(name = "userID", value = "投票id", required = true)
                                                   @PathVariable("userID") Long userID) {
        List<Long> list;
        try {
            list = voteService.findVote(userID);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getvote failed");
        }
    }

    @PostMapping("/postvote")
    public Result<String> insertNewVote(@ApiParam(name="userId", value="用户id", required = true)
                                                @RequestParam("userId") Long userID,
                                            @ApiParam(name="option_id", value="选项id", required = true)
                                                @RequestParam("option_id") Long option_id) {
        try {
            int res = voteService.insertOneVote(userID,option_id);
            return Result.success("插入数据成功！");
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getvote failed");
        }
    }
}

