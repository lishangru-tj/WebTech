package com.example.backend.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.backend.common.Result;
import com.example.backend.entity.Task;
import com.example.backend.service.RelativeService;
import com.example.backend.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */

@Api(tags = {"Task"})
@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private RelativeService relativeService;



    @ApiOperation("根据年、月信息返回事项列表（不带亲戚）")
    @GetMapping("findmonth")
    public Result<List<Task>> findTaskListByMonth(@ApiParam(name="userId", value="用户id", required = true)
                                                      @RequestParam("year") Long userId,
                                                    @ApiParam(name="year", value="事项在某个year能够出现", required = true)
                                                  @RequestParam("year") int year,
                                                  @ApiParam(name="month", value="事项在某个month能够出现", required = true)
                                                  @RequestParam("month") int month)
    {
        try {
            List<Task> taskList = taskService.findTaskByMonth(userId, year, month);
            return Result.success(taskList);
        }
        catch (Exception e){
            return Result.fail(500, "获取该月份的事项失败！");
        }
    }

//    @ApiOperation("返回task表的所有数据（不带亲戚）")
//    @GetMapping("findall")
//    public Result<List<Task>> findAllTask()
//    {
//        return taskService.findAll();
//    }

    @ApiOperation("返回一个task和它的所有亲戚")
    @GetMapping("findOneTaskAndRelative/{taskId}")
    public Result<Task> findTaskAndRelative(@ApiParam(name="taskId", value="事项id", required = true)
                                                @PathVariable("taskId") Long taskId)
    {
        try {
            Task task = taskService.findOneTaskAndRelative(taskId);
            return Result.success(task);
        }
        catch(Exception e){
            return Result.fail(500,e.getMessage());
        }
    }

    @ApiOperation("返回所有task和它的所有亲戚，且只有父事项被返回")
    @GetMapping("findAllTaskAndRelative/{userId}")
    public Result<List<Task>> findAllTaskAndRelative(@ApiParam(name="userId", value="用户id", required = true)
                                                         @PathVariable("userId") Long userId)
    {
        try {
            List<Task> taskList = taskService.findAllTaskAndRelative(userId);
            return Result.success(taskList);
        }
        catch (Exception e){
            return Result.fail(500,e.getMessage());
        }
    }

    @ApiOperation("插入一条新的task")
    @PostMapping("addOneNewTask")
    public Result<Long> insertOneNewTask(@RequestBody Task task)
    {
        try {
            Long newID = taskService.insertOneNewTask(task);
            return Result.success(newID);
        }
        catch (Exception e){
            return Result.fail(500,e.getMessage());
        }
    }

    @ApiOperation("更新事项")
    @PatchMapping("patchOneTask")
    public Result<String> patchOneTask(@RequestBody Task task)
    {
        return taskService.patchOneTask(task);
    }

    @ApiOperation("根据ID删除事项，并删除它在relativetask表中的所有关系")
    @DeleteMapping("removeOneTask")
    public Result<String> removeOneTask(@ApiParam(name="id", value="要删除的事项id", required = true)
                                            @RequestParam("id") Long id)
    {
        try {
            //先删除该事项在relative表里所有的存在
            int deleteRelativeRes = relativeService.deleteOneTaskRelative(id);

            //在删除该事项
            int deleteTaskRes = taskService.deleteById(id);

            return Result.success("从事项表以及亲戚表中彻底抹除了这个事项！");
        }
        catch (Exception e){
            return Result.fail(500,e.getMessage());
        }
    }

    @ApiOperation("添加一个子事项")
    @PostMapping("addOneSonTask")
    public Result<String> addOneSonTask(@ApiParam(name="taskId", value="父事项id", required = true)
                                            @RequestParam("taskId") Long taskId,
                                        @RequestBody Task sonTask)
    {

        try {
            //先拿到父事项
            Task fatherTask = taskService.findOneTaskAndRelative(taskId);

            //post新的子事项
            Long aaa = taskService.insertOneNewTask(sonTask);
            Long sonId = sonTask.getTaskId();

            //在relative表里新增一项
            int res = relativeService.insertOneRelative(taskId, sonId);
            return Result.success("成功添加子事项！");
        }
        catch(Exception e)
        {
            return Result.fail(500, e.getMessage());
        }

    }
}
