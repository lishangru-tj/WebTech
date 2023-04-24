package com.example.backend.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author: 杨严
 * @Date: 2022/11/26/12:53 PM
 * @Description:用户在这个活动中的身份
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityUserRole {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long activity_id;
    private Long user_id;
    private Boolean is_creator;
    private Boolean is_applicant;

    private Boolean is_member;


}
