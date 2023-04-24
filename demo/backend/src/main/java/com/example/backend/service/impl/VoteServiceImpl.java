package com.example.backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Classification;
import com.example.backend.entity.Vote;
import com.example.backend.entity.VoteOption;
import com.example.backend.mapper.VoteMapper;
import com.example.backend.mapper.VoteOptionMapper;
import com.example.backend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteMapper voteMapper;
    public List<Long> findVote(Long userID){
        QueryWrapper<Vote> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("user_id",userID);
        List<Vote> list=voteMapper.selectList(queryWrapper);
        List<Long> optionIDs=new ArrayList<>();
        for(Vote vote:list)
            optionIDs.add(vote.getOptionID());
        return optionIDs;
    }
    public int insertOneVote(Long userID,Long option_id){
        Vote newVote = new Vote(userID, option_id);
        int res = voteMapper.insert(newVote);
        return res;
    }

}
