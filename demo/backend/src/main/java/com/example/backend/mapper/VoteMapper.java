package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Vote;
import com.example.backend.entity.VoteOption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteMapper  extends BaseMapper<Vote> {
}
