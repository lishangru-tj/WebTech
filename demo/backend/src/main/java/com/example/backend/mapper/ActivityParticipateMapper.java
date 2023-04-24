package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ActivityParticipate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/9:03 AM
 * @Description:
 */
@Mapper
public interface ActivityParticipateMapper extends BaseMapper<ActivityParticipate> {
    @Delete(
            "DELETE " +
            "FROM activity_participate " +
            "WHERE activity_id=${activity_id}")
    Integer DeleteAct(@Param("activity_id") Long activity_id);

    @Select(
            "SELECT * " +
                    "FROM activity_participate inner join user " +
                    "ON activity_participate.user_id = user.id  " +
                    "WHERE activity_id=${activity_id}")
    List<Map<String,Object>> SelectActApplicantList(@Param("activity_id") Long activity_id);



    @Select("SELECT activity_id FROM activity_participate WHERE user_id=${user_id}")
    List<Long> SelectOneUserAllActivityId(@Param("user_id") Long user_id);
}
