package com.example.backend.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisData {
    //数量
    private int finishedStateNum;
    private int doingStateNum;
    private int delayingStateNum;
    private int delayedFinishedStateNum;

    //比率
    private float finishRate;//在所有事项中，已经完成的事项占总事项的比例
    private float timelyRate;//在已经完成的事项中，按时完成的事项占的比例
    private float delayRate;//在所有事项中，延期完成和延期中的事项占的比例

    //每天完成的数据
    private Map<Integer, Integer> oneMonthFinishedNumMap;
    //每天新建的数据
    private Map<Integer, Integer> oneMonthNewCreatedNumMap;

    //当前月的天数
    private int daysOfMonth;
}
