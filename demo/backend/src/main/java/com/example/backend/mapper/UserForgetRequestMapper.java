package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.UserForgetRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserForgetRequestMapper extends BaseMapper<UserForgetRequest> {
    
}
