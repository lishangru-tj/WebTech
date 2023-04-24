package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: 杨严
 * @Date: 2022/11/22/8:45 PM
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "activity",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity {

    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID, value ="activity_id")
    private Long activity_id;

    @TableField("title_name")
    private String title_name;

    @TableField("detail_text")
    private String detail_text;

    @TableField( "summary")
    private String summary;



    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd hh:mm:ss")
    @TableField( "start_time")
    private Date start_time;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd hh:mm:ss")
    @TableField( "end_time")
    private Date end_time;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd hh:mm:ss")
    @TableField( "create_time")
    private Date create_time;


    @TableField( "limit_capacity")
    private Boolean limit_capacity;

    @TableField( "capacity")
    private Integer capacity;



    @TableField( "hit_num")
    private Integer hit_num;

    @TableField( "mark")
    private Double mark;

    @TableField( "state")
    private Short state;

    @TableField( "creator_id")
    private Long creator_id;

    @TableField( "address_formatted")
    private String address_formatted;
    @TableField( "address_name")
    private String address_name;

    @TableField( "longitude")
    private Double longitude;

    @TableField( "latitude")
    private Double latitude;

    @TableField( "images")
    private String images;

    @TableField( "is_remind")
    private int isRemind;





}
