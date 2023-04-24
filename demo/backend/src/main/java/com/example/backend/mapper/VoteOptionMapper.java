package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import com.example.backend.entity.VoteOption;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface VoteOptionMapper extends BaseMapper<VoteOption> {
  @Update("UPDATE vote_option SET voteNum='${voteNum}+1' WHERE optionID = '${optionID}'")
  int updateVotes(@Param("optionID") Long optionID);
}
