package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@AllArgsConstructor
@NoArgsConstructor
@TableName("message_board")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageBoard {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long messageBoardId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long activityId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;
    private String content;
    private Date createTime;

    public MessageBoard(Long activityId, Long userId, String content, Date createTime) {
        this.activityId = activityId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }
}
