package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/6
 * @JDKVersion 17.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormatMessageBoard {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long messageBoardId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;
    private String userName;
    private String content;
    private Date createTime;
}
