package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityApply;
import com.example.backend.entity.Report;
import com.example.backend.entity.User;
import com.example.backend.service.ActivityService;
import com.example.backend.service.ReportService;
import com.example.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/13/9:39 AM
 * @Description:
 */
@Api(tags={"Report"})
@RestController
@RequestMapping("report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @ApiOperation("获取具体状态的举报单列表信息")
    @GetMapping("getNumByState")
    public Result<Map<String,Object>> getNumByState(
            @ApiParam(name = "state", value = "举报单的状态,0是未审核，1是已审核，2是全部", required = true)
            @RequestParam("state") String state
    ){
        Map<String,Object> map=new HashMap<>();
        String s=null;
        if (state.equals("0")||state.equals("1")){
            s=state;
        }
        else if (state.equals("2")){
            s=null;
        }
        else {
            return Result.fail(2001,"请输入正确的state 0,1,2");
        }
        try
        {
            Map<String, Object> num_map=reportService.getReportListNumByState(s);
            map.put("activity",num_map.get("activity"));
            map.put("user",num_map.get("user"));
            map.put("state",state);
            return Result.success(map);
        }
        catch (Exception e){
            return Result.fail(500,e.getMessage());
        }

    }


    @ApiOperation("依据条件筛选，获取举报单列表")
    @GetMapping("getReports")
    public Result<Map<String,Object>> getReports(
            @ApiParam(name = "state", value = "举报单的状态,0是未审核，1是已审核，2是全部", required = true)
            @RequestParam("state") String state,
            @ApiParam(name = "target_type", value = "举报单对象,0是活动，1是用户，2是全部", required = true)
            @RequestParam("target_type") String target_type,
            @ApiParam(name = "sortByTime", value = "按照举报单的创建时间排序,取值desc/asc", required = false)
            @RequestParam("sortByTime") String sortByTime
    ) {
        Map<String,Object> map=new HashMap<>();
        map.put("sortByTime",sortByTime);

        String default_sortByTime="desc";

        if (sortByTime!=null&&(sortByTime.equals("desc")||sortByTime.equals("asc"))){

        }
        else {
            sortByTime=default_sortByTime;
        }
        String s=null;
        if (state.equals("0")||state.equals("1")){
            s=state;
        }
        else if (state.equals("2")){
            s=null;
        }
        else {
            return Result.fail(2001,"请输入正确的state 0,1,2");
        }
        String t=null;
        if (target_type.equals("0")||target_type.equals("1")){
            t=target_type;
        }
        else if (target_type.equals("2")){
            t=null;
        }
        else {
            return Result.fail(2001,"请输入正确的target_type 0,1,2");
        }

        try{
            Map<String, Object> res_map=reportService.getReportList(s,t,sortByTime);
            map.put("reports",res_map.get("reports"));
            map.put("state",state);
            map.put("target_type",target_type);

            return Result.success(map);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,e.getMessage());
        }

    }


    @ApiOperation("获取一个举报单信息")
    @GetMapping("getOneReport")
    public Result<Map<String,Object>> getOneReport(
            @ApiParam(name = "report_id", value = "举报单ID", required = true)
            @RequestParam("report_id") Long report_id
    ) {
        Map<String,Object> map=new HashMap<>();

        try{
            Map<String, Object> res_map=reportService.getReport(report_id);
            Report report=(Report)res_map.get("report");
            Long activity_id=report.getActivityId();
            Long user_id=report.getUserId();
            if (activity_id!=null){
                Activity activity=activityService.getAct(activity_id);
                map.put("activity",activity);
            }
            if (user_id!=null){
                User user=userService.findUser(user_id);
                map.put("user",user);
            }

            map.put("report",report);
            map.put("report_id",report_id);

            return Result.success(map);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,e.getMessage());
        }

    }



    @ApiOperation("修改举报单的状态信息")
    @PatchMapping("/patchOneReport")
    public Result<Map<String,Object>> patchOneReport(
            @ApiParam(name = "report_id", value = "举报单ID", required = true)
            @RequestParam("report_id") Long report_id,
            @ApiParam(name = "state", value = "希望设置的举报单的状态,0是未审核，1是已审核", required = true)
            @RequestParam("state") String state,
            @ApiParam(name = "admin_id", value = "管理员ID", required = true)
            @RequestParam("admin_id") Long admin_id,
            @ApiParam(name = "handle_operation", value = "处理的操作", required = false)
            @RequestParam("handle_operation") String handle_operation
    ){
        Map<String,Object> map=new HashMap<>();
        if (state.equals("0")||state.equals("1")){

        }
        else {
            return Result.fail(2001,"请输入正确的state 0,1");
        }

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        System.out.println("当前时间：" + sdf.format(now));

        try{
            /** change report state */
            Map<String, Object> res_map=reportService.changeReportState(state,handle_operation,admin_id,report_id,now);
            map=res_map;
            if (map.get("i").equals(1)){
                return Result.success(map);
            }
            else {
                return Result.fail(2001,"没有report被修改");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,e.getMessage());
        }


    }


    @ApiOperation("提交一条举报单")
    @PostMapping("postOneReport")
    public Result<Map<String,Object>> postOneReport(
            @RequestBody Report report
    ){
        Map<String,Object> map=new HashMap<>();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        System.out.println("当前时间：" + sdf.format(d));

        if (report.getReportTime()==null){
            report.setReportTime(d);
        }

        try{
            Map<String, Object> res_map=reportService.addReport(report);
            map=res_map;
            if (map.get("i").equals(1)){
                return Result.success(map);
            }
            else {
                return Result.fail(2001,"新增report失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,e.getMessage());
        }


    }

}
