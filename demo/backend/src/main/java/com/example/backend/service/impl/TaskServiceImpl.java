package com.example.backend.service.impl;

import com.example.backend.Tools.DateTimeUtil;
import com.example.backend.common.Result;
import com.example.backend.entity.Task;
import com.example.backend.mapper.TaskMapper;
import com.example.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;


    @Override
    public Result<Task> findTask(Long task_id) {
        Task task = taskMapper.selectById(task_id);//利用mybatis-plus的单表查询，自己不用写SQL语句
        //没有找到，返回错误码
        if(task == null){
            return Result.fail(404,"事项不存在");
        }

        //找到用户，返回正确信息
        return Result.success(task);
    }

    @Override
    public List<Task> findTaskByMonth(Long userId, int year, int month) throws ParseException {
        int daysOfMonth = DateTimeUtil.getDayNumOfMonth(year, month);
        List<Task> taskList = taskMapper.selectByMonth(userId, year, month, daysOfMonth);
        return taskList;
    }

    @Override
    public Result<List<Task>> findAll() {
        List<Task> taskList = taskMapper.selectList(null);
        return Result.success(taskList);
    }

    @Override
    public Task findOneTaskAndRelative(Long task_id)
    {
        //因为做了join，一个task_id会对应好几项，所以这里用的是List
        List<Task> taskList = taskMapper.selectOneTaskAndRelative(task_id);
//        if(taskList == null || taskList.size() == 0) {
//            System.out.println(taskList.toString());
//            return Result.fail(404, "找不到该事项！");
//        }
        if(taskList.size() == 0)//不加就报500
            return null;

            Task taskResult = taskList.get(0);
            //把相同的taskId进行合并，就是合并它们的relativeTask
            if (taskList.size() > 1) {
                for (int i = 1; i < taskList.size(); i++) {
                    taskResult.addOneRelativeTask(taskList.get(i).getRelativeTask().get(0));
                }
            }


            if (taskResult.getRelativeTask() != null) {
                if (taskResult.getRelativeTask().size() != 0) {
                    //不应该返回已被删除的子事项
                    for (int i = taskResult.getRelativeTask().size() - 1; i >= 0; i--) {
                        if (!taskResult.getRelativeTask().get(i).getIsInDustbin().equals("0")) {
                            System.out.println("aaaaaaaaaaaaaaaaaaaaa" + taskResult.getRelativeTask().get(i).getIsInDustbin());
                            taskResult.getRelativeTask().remove(i);
                        }
                    }
                }
            }


            return taskResult;

    }

    @Override
    public List<Task> findAllTaskAndRelative(Long userId) {

        List<Task> taskList = taskMapper.selectAllTaskAndRelative(userId);


        List<Task> taskListResult = new ArrayList<>();
        List<Long> okIdList = new ArrayList<>();

        Task currentTask = null;
        for (Task t : taskList) {
            if (!okIdList.contains(t.getTaskId())) {
                if (currentTask != null)
                    taskListResult.add(currentTask);
                currentTask = t;
                okIdList.add(t.getTaskId());
            }
            //把相同的taskId进行合并，就是合并它们的relativeTask
            //短路运算
            else if(currentTask != null && t.getRelativeTask() != null && t.getRelativeTask().size() != 0)
                currentTask.addOneRelativeTask(t.getRelativeTask().get(0));
        }

        //短路运算
        if (currentTask != null && currentTask.getRelativeTask() != null) {
            if (currentTask.getRelativeTask().size() != 0) {
                //不应该返回已被删除的子事项
                for (int i = currentTask.getRelativeTask().size() - 1; i >= 0; i--) {
                    //短路运算
                    if (currentTask != null && !currentTask.getRelativeTask().get(i).getIsInDustbin().equals("0")) {
                        currentTask.getRelativeTask().remove(i);
                    }
                }
            }
        }

        if (currentTask != null)
            taskListResult.add(currentTask);
        return taskListResult;

    }

    @Override
    public Long insertOneNewTask(Task task) {
        task.setIsInDustbin("0");
        task.setCreateTime(DateTimeUtil.getCurrentTimestamp());
        //如果post的事项没有填入分组，则自动归入默认分组
        if(task.getClassificationTitle() == null || task.getClassificationTitle().equals(""))
            task.setClassificationTitle("默认分组");
        //如果post的事项没有填入tag，则置为“无”
        if(task.getTag() == null || task.getTag().equals(""))
            task.setTag("无");
        Long newID = Long.valueOf(taskMapper.insert(task));

        return newID;
    }

    @Override
    public int deleteById(Long id)
    {
        int resultCount = taskMapper.deleteById(id);
        return resultCount;
    }

    @Override
    public Result<String> patchOneTask(Task task) {
        //如果是否完成发生变化，则要记录真实完成时间
        Task oldTask = taskMapper.selectById(task.getTaskId());
        //1: 如果之前没完成，更新后完成了：更新真实完成时间，并把所有孩子也完成了
        if(oldTask.getTaskState() == 0 && task.getTaskState() != 0) {
            //更新真实完成时间
            task.setRealFinishTime(DateTimeUtil.getCurrentTimestamp());

            if(task.getRelativeTask() != null) {
                //完成所有孩子（递归）
                for (Task sonTask : task.getRelativeTask()) {
                    //如果及时完成
                    if (DateTimeUtil.getCurrentTimestamp().before(sonTask.getEndTime())) {
                        sonTask.setTaskState((short) 1);
                        sonTask.setRealFinishTime(DateTimeUtil.getCurrentTimestamp());
                    }
                    //如果没有及时完成
                    else {
                        sonTask.setTaskState((short) 2);
                        sonTask.setRealFinishTime(DateTimeUtil.getCurrentTimestamp());
                    }

                    //递归更新子事项
                    patchOneTask(sonTask);
                }
            }
        }

        //2: 如果之前完成了，更新后没完成，删掉真实完成时间
        else if(oldTask.getTaskState() != 0 && task.getTaskState() == 0)
            task.setRealFinishTime(null);

        System.out.println("aaaaaaaaaaaaaaaaaaaaaa"+task);

        int resultCount = taskMapper.updateById(task);
        if(resultCount == 0)
            return Result.fail(500,"更新数据失败！");
        return Result.success("更新数据成功！");
    }

    @Override
    public List<Task> selectOneUserOneSortAllTask(Long userId, String classificationTitle) {
        List<Task> taskList = taskMapper.selectOneUserOneSortAllTask(userId, classificationTitle);
        return taskList;
    }

    /**
     * @description:获取一个月内完成的事项列表
     * @author: hym
     * @date: 2022/11/30 10:32
     * @param: userId
     * @param: year
     * @param: month
     * @param: day
     * @return: java.util.List<com.example.backend.entity.Task>
     **/
    @Override
    public List<Task> selectOneDayFinishedTaskList(Long userId, int year, int month, int day) {
        //保证一位数的日期也是dd格式
        String dayStr = day < 10 ? "0" + Integer.toString(day) : Integer.toString(day);

        //保证一位数的月份也是mm格式
        String monthStr = month < 10 ? "0" + Integer.toString(month) : Integer.toString(month);

        List<Task> taskList = taskMapper.selectOneDayFinishedTaskList(userId, year, monthStr, dayStr);
        return taskList;
    }

    /**
     * @description:获取一个月内新建的事项列表
     * @author: hym
     * @date: 2022/11/30 10:32
     * @param: userId
     * @param: year
     * @param: month
     * @param: day
     * @return: java.util.List<com.example.backend.entity.Task>
     **/
    @Override
    public List<Task> selectOneDayCreatedTaskList(Long userId, int year, int month, int day){
        //保证一位数的日期也是dd格式
        String dayStr = day < 10 ? "0" + Integer.toString(day) : Integer.toString(day);

        //保证一位数的月份也是mm格式
        String monthStr = month < 10 ? "0" + Integer.toString(month) : Integer.toString(month);

        List<Task> taskList = taskMapper.selectOneDayCreatedTaskList(userId, year, monthStr, dayStr);
        return taskList;
    }


    @Override
    public List<Task> findAllTask() {
        List<Task> taskList = taskMapper.selectList(null);
        return taskList;
    }

    @Override
    public int modifyTaskStatus(Long taskId, int status) {
        return taskMapper.updateTaskRemind(taskId, status);
    }
}
