package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.Task;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author hym
 * @Date 2022/11/19
 * @Description 事项Service
 */
@Service
public interface TaskService {
    Result<Task> findTask(Long task_id);
    List<Task> findTaskByMonth(Long userId, int year, int month) throws ParseException;
    Result<List<Task>> findAll();
    Task findOneTaskAndRelative(Long task_id);
    List<Task> findAllTaskAndRelative(Long userId);
    Long insertOneNewTask(Task task);
    int deleteById(Long id);
    Result<String> patchOneTask(Task task);

    List<Task> selectOneUserOneSortAllTask(Long userId, String classificationTitle);
    List<Task> selectOneDayFinishedTaskList(Long userId, int year, int month, int day);
    List<Task> selectOneDayCreatedTaskList(Long userId, int year, int month, int day);


    List<Task> findAllTask();

    int modifyTaskStatus(Long taskId, int status);


}
