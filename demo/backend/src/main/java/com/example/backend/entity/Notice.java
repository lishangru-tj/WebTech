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
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notice")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notice {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long noticeId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long activityId;
    private String title;
    private String content;
    private Date createTime;
    private int type;

    public Notice(String title, String content, Date createTime, int type) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.type = type;
    }

    public Notice(Long activityId, String title, String content, Date createTime, int type) {
        this.activityId = activityId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.type = type;
    }
}
