package com.example.backend.controller;

import com.example.backend.dto.AnalysisData;
import com.example.backend.Tools.DateTimeUtil;
import com.example.backend.common.Result;
import com.example.backend.entity.Task;
import com.example.backend.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
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
@Api(tags = {"DataAnalyze"})
@RestController
@RequestMapping("dataAnalyze")
public class DataAnalysisController {
    @Autowired
    TaskService taskService;

    @ApiOperation("获取各项数据分析")
    @GetMapping("findAnalysisData")
    public Result<AnalysisData> findAnalysisData(@ApiParam(name="userId", value="用户id", required = true)
                                                    @RequestParam("userId") Long userId,
                                                @ApiParam(name="year", value="年", required = true)
                                                    @RequestParam("year") int year,
                                                @ApiParam(name="month", value="月", required = true)
                                                    @RequestParam("month") int month)
    {
        try {
            List<Task> taskList = taskService.findTaskByMonth(userId, year, month);

            //1：计算各项事项数
            int finishedStateNum = 0;
            int doingStateNum = 0;
            int delayingStateNum = 0;
            int delayedFinishedStateNum = 0;

            //根据数据库存储的状态，转化为当前状态
            for (Task t : taskList) {
                short dbState = t.getTaskState();
                if (dbState == 1)
                    finishedStateNum++;
                else if (dbState == 2)
                    delayedFinishedStateNum++;
                else if (dbState == 0) {
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    if (currentTime.before(t.getEndTime()))
                        doingStateNum++;
                    else
                        delayingStateNum++;
                }
            }

            //当前月的天数
            int dayNumOfMonth = DateTimeUtil.getDayNumOfMonth(year, month);
            //2：计算一个月内的事项完成情况
            Map<Integer, Integer> oneMonthFinishedNumMap = new HashMap<>();
            for(int day=1; day<=dayNumOfMonth; day++)
            {
                List<Task> oneDayTaskList = taskService.selectOneDayFinishedTaskList(userId, year, month, day);
                int finishedNumOfOneDay = oneDayTaskList.size();
                oneMonthFinishedNumMap.put(day, finishedNumOfOneDay);
            }


            //3：计算一个月内的事项新建情况
            Map<Integer, Integer> oneMonthNewCreateNumMap = new HashMap<>();
            for(int day=1; day<=dayNumOfMonth; day++)
            {
                List<Task> oneDayTaskList = taskService.selectOneDayCreatedTaskList(userId, year, month, day);
                int createdNumOfOneDay = oneDayTaskList.size();
                oneMonthNewCreateNumMap.put(day, createdNumOfOneDay);
            }

            //4：计算几个”率“
            float finishRate = calculateFinishRate(taskList);
            float timelyRate = calculateTimelyRate(taskList);
            float delayRate = calculateDelayRate(taskList);



            AnalysisData analysisData = new AnalysisData(finishedStateNum, doingStateNum, delayingStateNum, delayedFinishedStateNum,
                                                        finishRate, timelyRate, delayRate,
                                                        oneMonthFinishedNumMap,
                                                        oneMonthNewCreateNumMap,
                                                        dayNumOfMonth);
            return Result.success(analysisData);
        }
        catch(Exception e)
        {
            return Result.fail(500,e.getMessage());
        }

    }

    /*
     * @description: 获取完成率
     * @author: hym
     * @date: 2022/11/25 10:38
     * @param:
     * @param: taskList
     * @return:
     * @return: float
     **/
    private float calculateFinishRate(List<Task> taskList)
    {
        int totalCount = taskList.size();
        if(totalCount == 0)
            return 0;

        int finishCount = 0;

        for(Task t : taskList)
            if(t.getTaskState() != 0)
                finishCount++;

        return (float)finishCount / (float)totalCount;
    }

    /*
     * @description: 获取及时率
     * @author: hym
     * @date: 2022/11/25 10:38
     * @param:
     * @param: null
     * @return:
     * @return: null
     **/
    private float calculateTimelyRate(List<Task> taskList)
    {
        int timelyFinishNum = 0;
        int delayedFinishedNum = 0;



        for(Task t : taskList)
        {
            short dbState = t.getTaskState();
            if(dbState == 1)
                timelyFinishNum++;
            else if(dbState == 2)
                delayedFinishedNum++;
        }

        if(timelyFinishNum + delayedFinishedNum == 0)
            return 0;
        return (float)timelyFinishNum / (float)(timelyFinishNum + delayedFinishedNum);
    }

    /*
     * @description: 获取延期率
     * @author: hym
     * @date: 2022/11/25 10:42
     * @param:
     * @param: taskList
     * @return:
     * @return: float
     **/
    private float calculateDelayRate(List<Task> taskList)
    {
        int totalCount = taskList.size();
        if(totalCount == 0)
            return 0;

        int delayCount = 0;

        for(Task t : taskList)
        {
            short dbState = t.getTaskState();
            if(dbState == 2)
                delayCount++;


            Timestamp endTime = t.getEndTime();
            Timestamp currentTime = DateTimeUtil.getCurrentTimestamp();
            if(dbState == 0 && endTime != null ) {
                if(endTime.before(currentTime))
                    delayCount++;
            }
        }

        return (float)delayCount / (float)totalCount;
    }
}
