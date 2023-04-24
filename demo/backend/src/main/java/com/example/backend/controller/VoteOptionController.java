package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.VoteOption;
import com.example.backend.service.VoteOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"VoteOption"})
@RestController
@RequestMapping("vote_option")
public class VoteOptionController {

  @Autowired
  private VoteOptionService voteOptionService;

  @ApiOperation("获取投票选项")
  @GetMapping("/getoptions/{pollID}")
  public Result<List<VoteOption>> findVoteOption(@ApiParam(name = "pollID", value = "投票id", required = true)
                                                 @PathVariable("pollID") Long pollID) {
    List<VoteOption> list;
    try {
      list = voteOptionService.findVoteOption(pollID);
      return Result.success(list);
    } catch (Exception e) {
      return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getoptions failed");
    }
  }
  @ApiOperation("统计总票数")
  @PutMapping("/putTotal/{optionID}")
  public int putTotal(@ApiParam(name="optionID", value="选项id", required = true)
                      @PathVariable("optionID") Long optionID)
  {
    return voteOptionService.UpdateVotes(optionID);
  }
}
