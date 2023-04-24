package com.example.backend.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
/**
 * @Author: 杨严
 * @Date: 2022/11/27/11:50 PM
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("activity_mark")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityMark {

    @MppMultiId
    @TableField(value = "activity_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long activity_id;

    @MppMultiId
    @TableField(value = "user_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long user_id;

    @TableField("mark")
    private Integer mark;


    @TableField("mark_time")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd hh:mm:ss")
    private Date mark_time;
}
