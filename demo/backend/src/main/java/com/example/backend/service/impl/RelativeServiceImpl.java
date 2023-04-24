package com.example.backend.service.impl;

import com.example.backend.mapper.RelativeMapper;
import com.example.backend.service.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */

@Service
public class RelativeServiceImpl implements RelativeService {
    @Autowired
    RelativeMapper relativeMapper;

    @Override
    public int insertOneRelative(Long taskId, Long relativeTaskId) {
        int res = relativeMapper.insertOneNewRelative(taskId, relativeTaskId);
        return res;
    }

    @Override
    public int deleteOneTaskRelative(Long taskId) {
        int res = relativeMapper.deleteOneRelative(taskId);
        return res;
    }
}
