package com.example.backend.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author: 杨严
 * @Date: 2022/11/23/8:16 AM
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityBriefDto {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long activity_id;
    private String title_name;


}
