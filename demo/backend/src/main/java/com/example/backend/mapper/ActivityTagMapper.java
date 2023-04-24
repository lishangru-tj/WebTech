package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ActivityTag;
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
public interface ActivityTagMapper extends BaseMapper<ActivityTag> {
    @Delete(
            "DELETE " +
                    "FROM activity_tag " +
                    "WHERE activity_id=${activity_id}")
    Integer DeleteActAllTags(@Param("activity_id") Long activity_id);


    @Select("SELECT * " +
            "FROM tag inner join activity_tag " +
            "ON tag.tag_id = activity_tag.tag_id  " +
            "WHERE activity_id=${activity_id}")
    List<Map<String,Object>> SelectActAllTags(@Param("activity_id") Long activity_id);
}
