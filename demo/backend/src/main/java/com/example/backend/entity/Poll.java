package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="poll",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Poll{
  @JsonSerialize(using= ToStringSerializer.class)
  @TableId(type = IdType.ASSIGN_ID,value="poll_id")
  private Long pollID;
  @TableField("activity_id")
  private Long activity_id;
  @TableField("topic_text")
  private String topicText;

  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField("deadline")
  private LocalDateTime deadline;
  @TableField("multiple_choice")
  private boolean multipleChoice;

  public void setactivity_id(Long activity_id) {
    this.activity_id = activity_id;
  }

  public void setDeadline(LocalDateTime deadline) {
    this.deadline = deadline;
  }

  public void setmultipleChoice(boolean multipleChoice) {
    this.multipleChoice = multipleChoice;
  }

  public void settopicText(String topicText) {
    this.topicText = topicText;
  }
}
