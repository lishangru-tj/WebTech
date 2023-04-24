package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Data
@NoArgsConstructor
@TableName("classification")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Classification {
    private String classificationTitle;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    private Timestamp createTime;

    public Classification(String classificationTitle, Long userId) {
        this.classificationTitle = classificationTitle;
        this.userId = userId;
        this.createTime = new Timestamp(System.currentTimeMillis());
    }
}
