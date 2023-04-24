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
@TableName("item_notice")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemNotice {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Long itemNoticeId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String title;
    private String content;
    private Date createTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    public ItemNotice(Long userId, String title, String content, Date createTime, Long itemId, int status) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.itemId = itemId;
        this.status = status;
    }

    private int status;
}
