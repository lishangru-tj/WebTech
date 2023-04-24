package com.example.backend.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.backend.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Data
@NoArgsConstructor
@TableName("classification")
public class SortedTask {
    private String classificationTitle;
    private List<Task> taskList;
    private Timestamp createTime;
    private int numTask;

    public SortedTask(String classificationTitle, List<Task> taskList, Timestamp createTime)
    {
        this.classificationTitle = classificationTitle;
        this.taskList = taskList;
        this.createTime = createTime;
        this.numTask = taskList.size();
    }

}
