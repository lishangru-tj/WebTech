package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/01/7:16 PM
 * @Description:
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    @Select(
            "SELECT * " +
             "FROM tag ")
    List<Map<String,Object>> SelectAll();


}
