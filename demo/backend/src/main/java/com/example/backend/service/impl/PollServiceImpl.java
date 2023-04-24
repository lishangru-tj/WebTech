package com.example.backend.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.Poll;
import com.example.backend.mapper.PollMapper;
import com.example.backend.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PollServiceImpl implements PollService {
  @Autowired
  private PollMapper pollMapper;

  @Override
  @Transactional
  public Long createPoll(Long activity_id, String topicText, LocalDateTime deadline, boolean multipleChoice) {
    Poll poll=new Poll();
    try{poll.setactivity_id(activity_id);
      poll.setDeadline(deadline);
      poll.setmultipleChoice(multipleChoice);
      poll.settopicText(topicText);
      int newID=pollMapper.insert(poll);
      if(newID == Integer.MIN_VALUE + 1001) {
        return Long.valueOf(0);
      }
      return Long.valueOf(poll.getPollID());
    }
    catch(Exception e){
      return Long.valueOf(0);
    }
  }
  public List<Poll> findPoll(Long activity_id){
    QueryWrapper<Poll> queryWrapper=new QueryWrapper<>();
    queryWrapper
            .eq("activity_id",activity_id);
    List<Poll> list=pollMapper.selectList(queryWrapper);
    System.out.println(pollMapper.selectList(queryWrapper));

    return list;
  }
  @Override
  public Integer deletePoll(Long pollID){
    try{
      pollMapper.deleteById(pollID);
    return 1;
    }
    catch(Exception e){
      return 0;
    }
  }
}
