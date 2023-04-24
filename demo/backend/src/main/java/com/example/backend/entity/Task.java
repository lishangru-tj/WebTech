package com.example.backend.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import java.sql.Date;
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
@AllArgsConstructor
@NoArgsConstructor
@TableName("task")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using= ToStringSerializer.class)
    private Long taskId;
    private String taskTitle;
    private String taskDetail;
    private short taskState;
    private String classificationTitle;
    private short priority;
    private Timestamp startTime;
    private Timestamp endTime;
    private short isParent;
    private String isInDustbin;

    //允许将字段更新为空的注解
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Timestamp realFinishTime;
    private String tag;
    private Timestamp createTime;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @TableField(exist = false)
    private List<Task> relativeTask;

    @TableField("is_remind")
    private int isRemind;


    public void addOneRelativeTask(Task task)
    {
        relativeTask.add(task);
    }
}
