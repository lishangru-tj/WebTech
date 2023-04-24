package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Date: 2022/12/01/7:14 PM
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "activity_tag",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityTag {
    @JsonSerialize(using= ToStringSerializer.class)
    @MppMultiId
    @TableField(value = "activity_id")
    private Long activity_id;

    @MppMultiId
    @TableField(value = "tag_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long tag_id;



}
