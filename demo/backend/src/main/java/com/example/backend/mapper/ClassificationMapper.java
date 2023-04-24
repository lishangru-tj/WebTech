package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Classification;
import com.example.backend.entity.Task;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ClassificationMapper extends BaseMapper<Task> {
    @Select("SELECT * FROM classification WHERE user_id=${userId}")
    List<Classification> selectOneUserAllClassification(@Param("userId") Long userId);

    @Insert("INSERT INTO classification(user_id,classification_title,create_time) VALUES(#{userId},#{classificationTitle},#{createTime})")
    int insertOneClassification(Classification classification);

    @Delete("DELETE FROM classification WHERE user_id=#{userId} AND classification_title=#{classificationTitle}")
    int deleteOneClassification(Classification classification);
}
