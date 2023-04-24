package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/3
 * @JDKVersion 17.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormatMessage {
    private Long id;
    private String status;
    private String type;
    private Long sendTime;
    private String content;
    private double fileSize;
    private String fileName;
    private FormatUser fromUser;

    private String toContactId;
    public FormatMessage(Long id, String status, String type, Long sendTime, String content, FormatUser fromUser, String toContactId) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.sendTime = sendTime;
        this.content = content;
        this.fromUser = fromUser;
        this.toContactId = toContactId;
    }
}
