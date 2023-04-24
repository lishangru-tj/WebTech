package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 杨严
 * @Date: 2022/11/27/9:00 AM
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("activity_participate")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityParticipate {

    @MppMultiId
    @TableField(value = "activity_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long activity_id;


    @MppMultiId
    @TableField(value = "user_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long user_id;
}
