package com.example.backend.controller;

import com.alibaba.fastjson.JSON;
import com.example.backend.common.Result;
import com.example.backend.entity.Poll;
import com.example.backend.entity.options;
import com.example.backend.service.PollService;
import com.example.backend.service.VoteOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Api(tags = {"Poll"})
@RestController
@RequestMapping("poll")

public class PollController {
  @Autowired
  private PollService pollService;
  @Autowired
  private VoteOptionService voteOptionService;

  @ApiOperation("新建投票")
  @PostMapping("/postPoll")
  public Result<String> insertOneNewPoll(@RequestBody Map<String,Object> pollWithOption)
  {
    try {
      String topicText = JSON.toJSONString(pollWithOption.get("topic_text"));
      Long activity_id = Long.parseLong(JSON.toJSONString(pollWithOption.get("activity_id")).replace("\"", ""));
      boolean multipleChoice = Boolean.parseBoolean(JSON.toJSONString(pollWithOption.get("multipleChoice")));
      List<options> voteoptions = JSON.parseArray(JSON.toJSONString(pollWithOption.get("option")), options.class);
      LocalDateTime deadline = LocalDateTime.now();
      String deadline_input=JSON.toJSONString(pollWithOption.get("deadline_time")).replace("\"", "");
      if (deadline_input.length() > 4) {
        deadline=LocalDateTime.parse(deadline_input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      }
      Long pollID = pollService.createPoll(activity_id, topicText, deadline, multipleChoice);
      return voteOptionService.createVoteOptions(voteoptions, pollID);
    }
    catch(Exception e){
      return Result.fail(10001,"验证失败");
    }
  }

  @ApiOperation("获取投票")
  @GetMapping("/getPoll/{activity_id}")
  public Result<List<Poll>> findPoll(@ApiParam(name="activity_id", value="活动id", required = true)
                                     @PathVariable("activity_id") Long activity_id){
    List<Poll> list;
    try {
      list=pollService.findPoll(activity_id);
      return Result.success(list);
    }
    catch (Exception e){
      return Result.fail(HttpStatus.EXPECTATION_FAILED.value(),"findPoll failed");
    }
  }

  @ApiOperation("删除投票")
  @DeleteMapping("/deletePoll/{pollID}")
  public Integer removePoll(@ApiParam(name="pollID", value="投票id", required = true)
                                     @PathVariable("pollID") Long pollID){
    try {
      return pollService.deletePoll(pollID);
    }
    catch (Exception e){
      return 0;
    }
  }
}

