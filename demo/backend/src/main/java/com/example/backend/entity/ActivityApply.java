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
 * @Date: 2022/11/26/1:13 PM
 * @Description: 活动报名的联系表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "activity_apply",autoResultMap = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityApply {
    @JsonSerialize(using= ToStringSerializer.class)
    @MppMultiId
    @TableField(value = "activity_id")
    private Long activity_id;

    @MppMultiId
    @TableField(value = "user_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long user_id;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd hh:mm:ss")
    @TableField("apply_time")
    private Date apply_time;
}
