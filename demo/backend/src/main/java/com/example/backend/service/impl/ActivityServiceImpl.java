package com.example.backend.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.backend.common.Result;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityApply;
import com.example.backend.mapper.ActivityApplyMapper;
import com.example.backend.mapper.ActivityMapper;
import com.example.backend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 杨严
 * @Date: 2022/11/22/8:47 PM
 * @Description:
 */
@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityMapper activityMapper;



    @Override
    public List<Activity> getAll(){
        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();

        List<Activity> list=activityMapper.selectList(queryWrapper);
        System.out.println(activityMapper.selectList(queryWrapper));

        return list;
    }

    @Override
    public List<Map<String, Object>> getUserAllCreateActList(Long user_id, Short state) {
        /**mapper自定义SQL获取所有的活动*/
        List<Map<String,Object>> list=activityMapper.SelectCreateList(user_id);

        /**state传值非-1，则添加state filter*/
        list=list.stream().filter(act->{
            return state==-1?true:state.toString().equals(act.get("state").toString());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Map<String,Object>> getUserAllApplyActList(Long user_id,Short state) {
        /**mapper自定义SQL获取所有的活动*/
        List<Map<String,Object>> list=activityMapper.SelectApplyList(user_id);

        /**state传值非-1，则添加state filter*/
        list=list.stream().filter(act->{
                return state==-1?true:state.toString().equals(act.get("state").toString());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    @Transactional
    public List<Activity> getUserAllActList(Long creator_id){

        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("creator_id",creator_id);
        List<Activity> list=activityMapper.selectList(queryWrapper);
//        System.out.println(activityMapper.selectList(queryWrapper));

        return list;
    }


    @Override
    @Transactional
    public List<Activity> getTestActList() {
        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("summary","test");
        List<Activity> list=activityMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public Activity getAct(Long activity_id) {
        /**
         * hit_num++
         * */
        this.ActivityHitNumPlus(activity_id);
        Activity act=activityMapper.selectById(activity_id);
        System.out.println("public Activity getAct(Long activity_id)"+act);
        return act;
    }

    @Override
    @Transactional
    public Long CreateAct(Activity act) {
        Long newID;
        activityMapper.insert(act);
        newID=act.getActivity_id();
        return newID;
    }

    @Override
    public Integer DeleteAct(Long activity_id) {
        Integer delete_num;
        delete_num=activityMapper.deleteById(activity_id);
        //delete successful,delete one activity
        return delete_num;

    }

    @Override
    public Integer  UpdateAct(Activity act){
        int resultCount = activityMapper.updateById(act);
        return resultCount;
    }

    @Override
    public ActivityUserRole isCreator(Long user_id, Long activity_id) {
        ActivityUserRole role=new ActivityUserRole();
        role.setActivity_id(activity_id);
        role.setUser_id(user_id);

        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .eq("creator_id",user_id)
                .eq("activity_id",activity_id);
        Integer count=activityMapper.selectCount(queryWrapper);
        role.setIs_creator(count==1?true:false);
        return role;

    }


    @Override
    @Transactional
    public Integer SetActivityState(Long activity_id, Integer state_val) {
        UpdateWrapper<Activity> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("activity_id",activity_id).set("state",state_val);
        return activityMapper.update(null,updateWrapper);
    }

    @Override
    public List<Map<String, Object>> getUserAllParticipateActList(Long user_id,Short state) {
        List<Map<String,Object>> list=activityMapper.SelectParticipateList(user_id);
        list=list.stream().filter(act->{
            return state==-1?true:state.toString().equals(act.get("state").toString());
        }).collect(Collectors.toList());

        return list;
    }


    @Override
    public List<Activity> GetMapActList(List<Double>Latitudes,List<Double>Longitudes) {
        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .between("latitude",Latitudes.get(0),Latitudes.get(1))
                .between("longitude",Longitudes.get(0),Longitudes.get(1));
        List<Activity> activityList=activityMapper.selectList(queryWrapper);


        return activityList;
    }

    @Override
    public String PatchActImages(String images,Long activity_id) {

        activityMapper.update(
                null,
                Wrappers.<Activity>lambdaUpdate()
                        .set(Activity::getImages,images)
                        .eq(Activity::getActivity_id,activity_id)
        );
        return images;
    }


    @Override
    public List<Activity> getAvailableActs(){
        QueryWrapper queryWrapper = new QueryWrapper<>();queryWrapper.eq("state", 0);
        List<Activity> list=activityMapper.selectList(queryWrapper);
        System.out.println(activityMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public Integer ActivityHitNumPlus(Long activity_id) {
        Activity act=activityMapper.selectById(activity_id);
        if (act==null){
            return -1;
        }

        activityMapper.update(
                null,
                Wrappers.<Activity>lambdaUpdate()
                        .set(Activity::getHit_num,act.getHit_num()+1)
                        .eq(Activity::getActivity_id,activity_id)
        );
        return act.getHit_num();
    }

    @Override
    public int modifyActivityRemind(Long activityId, int status) {
        return activityMapper.updateActivityRemind(activityId, status);
    }
}
