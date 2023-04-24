package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.DustbinTask;
import com.example.backend.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DustbinMapper extends BaseMapper<Task> {
    @Select("SELECT task_id,task_title,classification_title,is_in_dustbin,start_time,end_time FROM task WHERE is_in_dustbin<>'0' AND user_id=${userId}")
    List<DustbinTask> selectAllRubbish(@Param("userId") Long userId);

//    @Delete("DELETE FROM task WHERE is_in_dustbin<>'0' AND user_id=${userId}")
//    int deleteAllRubbish(@Param("userId") Long userId);

    @Update("UPDATE task SET is_in_dustbin='0' WHERE task_id=${id}")
    int restoreOneRubbish(Long id);
}
