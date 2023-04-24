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
import org.apache.ibatis.jdbc.Null;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/24
 * @JDKVersion 17.0.4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long messageId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;
    private int status;
    private int type;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId;
    private Long sendTime;
    private String content;

    public Message(Long activityId, Long senderId, int status, int type, Long sendTime) {
        this.activityId = activityId;
        this.senderId = senderId;
        this.status = status;
        this.type = type;
        this.sendTime = sendTime;
    }

    public Message(Long activityId, Long senderId, int status, int type, Long sendTime, String content) {
        this.activityId = activityId;
        this.senderId = senderId;
        this.status = status;
        this.type = type;
        this.sendTime = sendTime;
        this.content = content;
    }

    public Message(Long activityId, Long senderId, int status, int type, Long fileId, Long sendTime, String content) {
        this.activityId = activityId;
        this.senderId = senderId;
        this.status = status;
        this.type = type;
        this.fileId = fileId;
        this.sendTime = sendTime;
        this.content = content;
    }
}
