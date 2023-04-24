package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Relative;
import com.example.backend.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RelativeMapper extends BaseMapper<Relative> {
    @Insert("INSERT INTO relativetask(task_id,relative_task_id) VALUES(${taskId}, ${relativeTaskId})")
    int insertOneNewRelative(@Param("taskId") Long taskId, @Param("relativeTaskId") Long relativeTaskId);

    @Delete("DELETE FROM relativetask WHERE task_id=${taskId} OR relative_task_id=${taskId}")
    int deleteOneRelative(@Param("taskId") Long taskId);

    //找到父事项所有的子事项
    @Select("SELECT relative_task_id FROM relativetask WHERE task_id=${fatherTaskId}")
    List<Long> selectAllSonTaskId(@Param("fatherTaskId") Long fatherTaskId);
}
