package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ActivityMark;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/11:53 PM
 * @Description:
 */
@Mapper
public interface ActivityMarkMapper extends BaseMapper<ActivityMark> {


    @Select(
            "SElECT * " +
                    "FROM activity_mark " +
                    "WHERE activity_id=${activity_id} AND user_id=${user_id} ")
    List<Map<String,Object>> SelectActMark(@Param("activity_id") Long activity_id, @Param("user_id") Long user_id);

    @Select(
            "SElECT * " +
                    "FROM activity_mark " +
                    "WHERE activity_id=${activity_id} ")
    List<Map<String,Object>> SelectActMark(@Param("activity_id") Long activity_id);

}
