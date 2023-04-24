package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/22/9:12 PM
 * @Description:
 */



@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {



    @Select(
            "SELECT * " +
            "FROM activity inner join activity_apply " +
            "ON activity.activity_id = activity_apply.activity_id  " +
            "WHERE user_id=${user_id}")
    List<Map<String,Object>> SelectApplyList(@Param("user_id") Long user_id);


    @Select("SELECT * " +
            "FROM activity inner join activity_participate " +
            "ON activity.activity_id = activity_participate.activity_id  " +
            "WHERE user_id=${user_id}")
    List<Map<String,Object>> SelectParticipateList(@Param("user_id") Long user_id);

    @Select("SELECT * " +
            "FROM activity " +
            "WHERE creator_id=${user_id}")
    List<Map<String,Object>> SelectCreateList(@Param("user_id") Long user_id);




    @Update("UPDATE activity set is_remind=${status} WHERE activity_id=${activityId}")
    int updateActivityRemind(@Param("activityId") Long activityId, @Param("status") int status);


}
