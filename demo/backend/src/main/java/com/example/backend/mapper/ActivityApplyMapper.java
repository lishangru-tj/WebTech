package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ActivityApply;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/26/1:17 PM
 * @Description:
 */
@Mapper
public interface ActivityApplyMapper extends BaseMapper<ActivityApply> {

    @Delete(
            "DELETE " +
             "FROM activity_apply " +
             "WHERE activity_id=${activity_id}")
    Integer DeleteAct(@Param("activity_id") Long activity_id);


    @Select(
            "SELECT * " +
                    "FROM activity inner join activity_apply " +
                    "ON activity.activity_id = activity_apply.activity_id  " +
                    "WHERE activity.activity_id=${activity_id}")
    List<Map<String,Object>> SelectApplicantList(@Param("activity_id") Long activity_id);

}
