package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_notice")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotice {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long noticeId;
    private int status;
}
