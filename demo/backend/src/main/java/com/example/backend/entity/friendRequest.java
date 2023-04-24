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

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend_request")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class friendRequest {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId
    private Long friendRequestId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userid;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long friendid;
    private Date createTime;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long activityId;
    private int status;
}
