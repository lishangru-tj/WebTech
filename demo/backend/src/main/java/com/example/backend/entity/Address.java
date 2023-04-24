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

/**
 * @Author: 杨严
 * @Date: 2022/11/30/1:01 PM
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long address_id;

    private String address_formatted;
    private String address_name;
    private String province;
    private String district;
    private String township;
    private Double longitude;
    private Double latitude;

}
