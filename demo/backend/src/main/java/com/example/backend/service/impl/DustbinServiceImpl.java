package com.example.backend.service.impl;

import com.example.backend.common.Result;
import com.example.backend.entity.DustbinTask;
import com.example.backend.entity.Task;
import com.example.backend.mapper.DustbinMapper;
import com.example.backend.mapper.RelativeMapper;
import com.example.backend.service.DustbinService;
import com.example.backend.service.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class DustbinServiceImpl implements DustbinService {
    @Autowired
    DustbinMapper dustbinMapper;

    @Autowired
    RelativeMapper relativeMapper;

    @Override
    public Result<List<DustbinTask>> findAllDustbinTask(Long userId) {
        List<DustbinTask> taskList = dustbinMapper.selectAllRubbish(userId);
        Collections.reverse(taskList);
        for(int i=0; i<taskList.size();i++)
        {
            if(taskList.get(i).getStartTime()==null || taskList.get(i).getStartTime()=="" || taskList.get(i).getStartTime().length()==0)
                taskList.get(i).setStartTime("无");

            if(taskList.get(i).getEndTime()==null || taskList.get(i).getEndTime()=="" || taskList.get(i).getEndTime().length()==0)
                taskList.get(i).setEndTime("无");

            if(taskList.get(i).getClassificationTitle()==null || taskList.get(i).getClassificationTitle()=="" || taskList.get(i).getClassificationTitle().length()==0)
                taskList.get(i).setClassificationTitle("默认分组");
        }
        return Result.success(taskList);
    }

    @Override
    public Result<String> smashOneRubbish(Long id) {
        //先删除子事项
        List<Long> sonTaskIdList = relativeMapper.selectAllSonTaskId(id);
        if(sonTaskIdList.size() > 0){
            for(Long sonId : sonTaskIdList){
                dustbinMapper.deleteById(sonId);
            }
        }

        int resultCount = dustbinMapper.deleteById(id);
        if(resultCount == 0)
            return Result.fail(500,"删除垃圾失败！");
        return Result.success("删除垃圾成功！");
    }

    @Override
    public Result<String> clearDustbin(Long userId) {
        List<DustbinTask> rubbishTaskList = dustbinMapper.selectAllRubbish(userId);
        if(rubbishTaskList.size() > 0){
            for(DustbinTask t : rubbishTaskList){
                Long fatherId = t.getTaskId();
                //先删除子事项
                List<Long> sonTaskIdList = relativeMapper.selectAllSonTaskId(fatherId);
                if(sonTaskIdList.size() > 0){
                    for(Long sonId : sonTaskIdList){
                        dustbinMapper.deleteById(sonId);
                    }
                }
                int resultCount = dustbinMapper.deleteById(fatherId);
                if(resultCount == 0)
                    return Result.fail(500,"删除垃圾失败！");
            }
        }

        return Result.success("清空回收站成功！");
    }

    @Override
    public Result<String> restoreOneRubbish(Long id) {
        int resultCount = dustbinMapper.restoreOneRubbish(id);
        if(resultCount == 0)
            return Result.fail(500,"恢复事项失败！");
        return Result.success("恢复事项成功！");
    }
}
