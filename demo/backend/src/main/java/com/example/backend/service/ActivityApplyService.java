package com.example.backend.service;

import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityApply;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/26/1:38 PM
 * @Description:
 */
@Service
public interface ActivityApplyService {
    /**判断用户是不是活动的报名者*/
    ActivityUserRole isApplicant(Long user_id, Long activity_id) ;

    /**用户报名活动,添加一条apply项*/
    Integer UserApplyAct(ActivityApply activityApply);

    /**用户取消报名，删除一条apply项*/
    Integer UserCancelApplyAct(Long activity_id,Long user_id);

    /**活动有多少人报名*/
    Integer CountApplicantNum(Long activity_id);

    /**获取活动的所有报名用户,列表*/
    List<ActivityApply> GetAllApplicantList(Long activity_id);



    /**删除某活动的所有记录**/
    Integer DeleteAct(Long activity_id);


    /**获取用户的所有报名*/
//    List<Activity> getUserApplyList(Long creator_id);



}
