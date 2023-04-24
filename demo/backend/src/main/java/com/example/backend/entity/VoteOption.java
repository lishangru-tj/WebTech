package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("vote_option")
public class VoteOption {
  @TableId(type = IdType.ASSIGN_ID,value="option_id")
  private Long optionID;
  @TableField("option_name")
  private String optionName;
  @TableField("poll_id")
  private Long pollID;
  @TableField(value="vote_num",update ="%s+1",updateStrategy = FieldStrategy.IGNORED)
  private int voteNum;

  public String getoptionName() {
    return optionName;
  }
}
