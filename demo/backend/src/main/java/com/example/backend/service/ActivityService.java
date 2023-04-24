package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/11/22/8:46 PM
 * @Description:
 */
@Service
public interface ActivityService {

    /**获取所有可报名的活动*/
    List<Activity> getAvailableActs();

    List<Activity> getAll();





    /**获取活动实体的列表，用户所有的*/
    List<Activity> getUserAllActList(Long creator_id);



    /**获取活用户所有的报名活动*/
    List<Map<String,Object>> getUserAllApplyActList(Long user_id,Short state);


    /**获取活用户所有的参与活动*/
    List<Map<String,Object>> getUserAllParticipateActList(Long user_id,Short state);



    /**获取活用户所有的创建的活动*/
    List<Map<String,Object>> getUserAllCreateActList(Long user_id,Short state);





    /**获取活动实体的列表，测试用的*/
    List<Activity> getTestActList();

    /**返回一个活动*/
    Activity getAct(Long activity_id);


    /**新建活动*/
    Long CreateAct(Activity act);

    /**删除活动*/
    Integer DeleteAct(Long activity_id);


    /**更新一个活动的信息,输入一个Activity，按照ID查找而更新*/
    Integer UpdateAct(Activity act);

    /**判断用户是不是活动的创建者*/
    ActivityUserRole isCreator(Long user_id, Long activity_id) ;


    /**修改活动的状态，指定为目标状态*/
    Integer SetActivityState(Long activity_id,Integer state_val);



    /**获取地图范围内的所有活动*/
    List<Activity> GetMapActList(List<Double>Latitudes,List<Double>Longitudes);

    /**活动的images，覆盖*/
    String PatchActImages(String images,Long activity_id);

    /**活动hit num ++*/
    Integer ActivityHitNumPlus(Long activity_id);




    int modifyActivityRemind(Long activityId, int status);

}
