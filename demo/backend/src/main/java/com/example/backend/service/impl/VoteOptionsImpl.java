package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.Poll;
import com.example.backend.entity.VoteOption;
import com.example.backend.entity.options;
import com.example.backend.mapper.VoteOptionMapper;
import com.example.backend.service.VoteOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VoteOptionsImpl implements VoteOptionService{
  @Autowired
  private VoteOptionMapper voteOptionMapper;
  public List<VoteOption> findVoteOption(Long pollID){

    QueryWrapper<VoteOption> queryWrapper=new QueryWrapper<>();
    queryWrapper
            .eq("poll_id",pollID);
    List<VoteOption> list=voteOptionMapper.selectList(queryWrapper);
    System.out.println(voteOptionMapper.selectList(queryWrapper));

    return list;
  }
  public int UpdateVotes(Long optionID){
    VoteOption voteOption =  voteOptionMapper.selectById(optionID);
    voteOptionMapper.updateById(voteOption);
    return 1;
  }
  public Result<String> createVoteOptions(List<options> voteOptions,Long pollID){
    for (int i=0;i<voteOptions.size();i++){
      VoteOption voteOption=new VoteOption();
      voteOption.setOptionName(voteOptions.get(i).getOption());
      voteOption.setPollID(pollID);
      voteOption.setVoteNum(0);
      int newID = voteOptionMapper.insert(voteOption);
      if(newID == Integer.MIN_VALUE + 1001) {
        return Result.fail(500, "插入数据失败！");
      }
    }
    return Result.success("插入数据成功！");
  }
}
