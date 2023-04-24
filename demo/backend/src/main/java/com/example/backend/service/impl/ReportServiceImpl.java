package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.backend.Tools.DateTimeUtil;
import com.example.backend.Tools.SystemNoticeUtil;
import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityApply;
import com.example.backend.entity.Report;
import com.example.backend.entity.User;
import com.example.backend.mapper.ReportMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.ActivityService;
import com.example.backend.service.ReportService;
import com.example.backend.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 杨严
 * @Date: 2022/12/13/9:23 AM
 * @Description:
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemNoticeUtil systemNoticeUtil;


    @Override
    public Map<String, Object> getReportList(String state, String target_type,String sortByTime) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(state != null, "state", state)
                .eq(target_type != null, "target_type", target_type);
        List<Report> reports = reportMapper
                .selectList(queryWrapper);
        // 按创建时间排序
        if (sortByTime.equals("asc")){
            List<Report> reports_sort = reports.stream().sorted
                    (Comparator.comparing(Report::getReportTime)).collect(Collectors.toList());

            reports=reports_sort;
        }
        else if  (sortByTime.equals("desc")){
            List<Report> reports_sort_reverse = reports.stream().sorted
                    (Comparator.comparing(Report::getReportTime).reversed()).collect(Collectors.toList());
            reports=reports_sort_reverse;

        }



        map.put("reports", reports);
        return map;
    }

    @Override
    public Map<String, Object> addReport(Report report) {
        Map<String, Object> map = new HashMap<>();
        Integer i = reportMapper.insert(report);
        map.put("report", report);

        map.put("i", i);
        return map;
    }

    @Override
    public Map<String, Object> changeReportState(String state,String handle_operation,Long admin_id,Long report_id,Date handle_time) {
        Map<String, Object> map = new HashMap<>();
        Report report=reportMapper.selectById(report_id);

        String state_=report.getState();

        Integer i = reportMapper.update(
                null,
                Wrappers.<Report>lambdaUpdate()
                        .eq(Report::getReportId, report_id)
                        .set(Report::getState, state)
                        .set(Report::getHandleTime,handle_time)
                        .set(Report::getHandleOperation,handle_operation)
                        .set(Report::getAdminId,admin_id)
        );
        map.put("i", i);
        map.put("state", state);
        /**
         * 举报单状态  0  ->  1
         * 且状态修改成功
         * 根据举报单信息 进行处理
         * */
        report=reportMapper.selectById(report_id);
        if (state_.equals("0")&&state.equals("1")&&i.equals(1)){
            Map<String, Object> handle_map=handleReport(report);
            map.put("handle",handle_map);
        }


        return map;
    }

    @Override
    public Map<String, Object> getReport(Long report_id) {
        Map<String, Object> map = new HashMap<>();
        Report report = reportMapper.selectById(report_id);
        map.put("report", report);
        return map;
    }


    @Override
    public Map<String, Object> getReportListNumByState(String state) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Report> queryWrapper_act = new QueryWrapper<>();
        QueryWrapper<Report> queryWrapper_user = new QueryWrapper<>();
        queryWrapper_act
                .eq("target_type", "0")
                .eq(state != null, "state", state);
        queryWrapper_user
                .eq("target_type", "1")
                .eq(state != null, "state", state);

        List<Report> act_list = reportMapper.selectList(queryWrapper_act);
        List<Report> user_list = reportMapper.selectList(queryWrapper_user);
        map.put("activity",act_list.size());
        map.put("user",user_list.size());

        return map;
    }

    @Override
    public Map<String, Object> handleReport(Report report) {
        Long activity_id=report.getActivityId();
        Long user_id=report.getUserId();
        Long admin_id=report.getAdminId();
        int banndeDay=Integer.parseInt(report.getHandleOperation());
        User admin=userService.findUser(admin_id);
        String handle_operation=report.getHandleOperation();
        /** handle act */
        if (activity_id!=null){
            /**
             * 1.删除活动
             * */
            Activity activity=activityService.getAct(activity_id);
            Long creator_id=activity.getCreator_id();
            if (handle_operation.equals("1")){
                /**删除活动本身*/
                int delete_act = activityService.DeleteAct(activity_id);
                if (delete_act == 1) {
                    /**向创建者发送通知*/
                    String title="您的活动受到举报，现经审核已删除";
                    String content="管理员<"+admin.getName()+">"+"经审核后删除了您的违规活动<"+activity.getTitle_name()+">";
                    systemNoticeUtil.SendSystemNotice(creator_id,title,content);

//                    return Result.success(res_map);
                } else {
//                    return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "DeleteAct FAILED!");
                }
            }


        } else if (user_id!=null) {
            /** handle user */
            /** 设置用户封禁天数 */
            //在原封禁天数的基础上加封
            User user=userMapper.selectById(user_id);
            Date nowTime=user.getBannedTime();

            if (nowTime==null||nowTime.before(new Date())){
                nowTime=new Date();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowTime);
            calendar.add(Calendar.DATE,banndeDay);
            Date targetDate = calendar.getTime();

            User user1=userMapper.selectById(user_id);

            userMapper.update(null,Wrappers.<User>lambdaUpdate()
                    .eq(User::getId,user_id)
                    .set(User::getBannedTime,targetDate)
                    .set(User::getCredit,user1.getCredit()-1));

        }

        return null;
    }


    @Override
    public String PatchReportImages(String images, Long report_id) {
        reportMapper.update(
                null,
                Wrappers.<Report>lambdaUpdate()
                        .set(Report::getImage,images)
                        .eq(Report::getReportId,report_id)
        );
        return images;
    }
}
