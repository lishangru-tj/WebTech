package com.example.backend.service.impl;

import com.example.backend.Tools.DateTimeUtil;
import com.example.backend.common.Result;
import com.example.backend.entity.Classification;
import com.example.backend.mapper.ClassificationMapper;
import com.example.backend.service.ClassificationService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ClassificationServiceImpl implements ClassificationService {
    @Autowired
    ClassificationMapper classificationMapper;

    @Override
    public List<Classification> findOneUserAllClassificationTitle(Long userId) {
        List<Classification> classificationList = classificationMapper.selectOneUserAllClassification(userId);

        //如果这个用户没有“默认分组”，则给这个用户加入“默认分组”
        if(classificationList == null || classificationList.size() == 0)
        {
            Classification defaultClassification = new Classification("默认分组",userId);
            classificationMapper.insertOneClassification(defaultClassification);

            //重新拿一次分组列表
            classificationList = classificationMapper.selectOneUserAllClassification(userId);
        }
        
        return classificationList;
    }

    @Override
    public int insertOneClassification(Long userId, String classificationTitle) {
        Classification newClass = new Classification(classificationTitle, userId);
        int res = classificationMapper.insertOneClassification(newClass);
        return res;
    }

    @Override
    public int deleteOneClassification(Long userId, String classificationTitle) {
        Classification deletedClass = new Classification(classificationTitle, userId);
        int res = classificationMapper.deleteOneClassification(deletedClass);
        return res;
    }
}
