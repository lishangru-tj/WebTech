package com.example.backend.controller;

import com.example.backend.Tools.DateTimeUtil;
import com.example.backend.Tools.SystemNoticeUtil;
import com.example.backend.common.Result;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.dto.ActivityBriefDto;
import com.example.backend.dto.ActivityDetailDto;
import com.example.backend.entity.*;
import com.example.backend.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 杨严
 * @Date: 2022/11/22/8:46 PM
 * @Description:
 */
@Api(tags = {"Activity"})
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemNoticeUtil systemNoticeUtil;
    @Autowired
    private ActivityApplyService activityApplyService;

    @Autowired
    private ActivityParticipateService activityParticipateService;

    @Autowired
    private ActivityTagService activityTagService;


    @Autowired
    private ActivityMarkService activityMarkService;

    @Autowired
    private ModelMapper modelMapper;


    @ApiOperation("查询用户是不是这个活动的参与者")
    @GetMapping("/ismember")
    public Result<ActivityUserRole> isMember(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            return Result.success(activityParticipateService.isMember(user_id, activity_id));
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "isMember FAILED");
        }

    }

    @ApiOperation("获取可以报名的活动")
    @GetMapping("/getAvailableActs")
    public Result<List<Activity>> getAvailableActs() {
        List<Activity> list;
        try {
            list = activityService.getAvailableActs();
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAvailableActs failed");
        }
    }


    @ApiOperation("获取所有活动")
    @GetMapping("/getAll")
    public Result<Map<String, Object>> GetAll() {
        List<Activity> activityList = activityService.getAll();
        Map<String, Object> map = new HashMap<>();
        map.put("all act", activityList);
        return Result.success(map);
    }


    @ApiOperation("Hello")
    @GetMapping("/HelloWorld")
    public Result<Map<String, Object>> HelloWorld() {
//        Map<String,Object> map=new HashMap<>();
//        map.put("hello","hello");
//        return Result.success(map);
        List<Activity> activityList = activityService.getAll();
        Map<String, Object> map = new HashMap<>();
        map.put("all act", activityList);
        return Result.success(map);
    }

    @ApiOperation("获取用户的所有报名的活动")
    @GetMapping("/getAllApply")
    public Result<List<Map<String, Object>>> getActList_UserAllApply(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "state", value = "活动状态", required = true)
            @RequestParam("state") Short state) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            map = activityService.getUserAllApplyActList(user_id, state);
            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAllApply failed");
        }
    }


    @ApiOperation("获取用户的所有参与的活动")
    @GetMapping("/getAllParticipate")
    public Result<List<Map<String, Object>>> getActList_UserAllParticipate(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "state", value = "活动状态", required = true)
            @RequestParam("state") Short state) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            map = activityService.getUserAllParticipateActList(user_id, state);
            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAllParticipate failed");
        }
    }

    @ApiOperation("获取用户所有的相关活动，报名、参与")
    @GetMapping("/getAllActList")
    public Result<List<Map<String, Object>>> getActList_UserALl(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "state", value = "活动状态", required = true)
            @RequestParam("state") Short state) {
        List<Map<String, Object>> map_all = new ArrayList<>();
        List<Map<String, Object>> map_apply = new ArrayList<>();
        List<Map<String, Object>> map_participate = new ArrayList<>();
        try {
            map_participate = activityService.getUserAllParticipateActList(user_id, state);
            map_apply = activityService.getUserAllApplyActList(user_id, state);
            map_all.addAll(map_apply);
            map_all.addAll(map_participate);
            return Result.success(map_all);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAll failed");
        }
    }


    @ApiOperation("获取用户的所有创建的活动")
    @GetMapping("/getAllCreate")
    public Result<List<Map<String, Object>>> getActList_UserAlleCreate(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "state", value = "活动状态", required = true)
            @RequestParam("state") Short state) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            map = activityService.getUserAllCreateActList(user_id, state);
            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAllCreate failed");
        }
    }


    @ApiOperation("获取测试用的活动概要")
    @GetMapping("/getActBriefTest/{id}")
    public Result<List<ActivityBriefDto>> getActBrief_Test(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable(name = "id") Long id) {
        List<Activity> list_act = activityService.getTestActList();
        List<ActivityBriefDto> list_actbrief = list_act.stream().map(act ->
        {
            return modelMapper.map(act, ActivityBriefDto.class);
        }).collect(Collectors.toList());

        return Result.success(list_actbrief);
    }

    @ApiOperation("获取一个活动的detail")
    @GetMapping("/getActDetail/{activity_id}")
    public Result<Map<String, Object>> getAct(
            @ApiParam(name = "activity_id", value = "活动id", required = true)
            @PathVariable(name = "activity_id") Long activity_id,
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id) {
//        System.out.println(id);
        Activity activity;
        Map<String, Object> res_map = new HashMap<>();
        try {
            /**获取Activity实体*/
            activity = activityService.getAct(activity_id);

            /**组装活动detail的dto*/
            ActivityDetailDto activityDetailDto = new ActivityDetailDto(activity);
            /**apply人数*/
            activityDetailDto.setApplicant_num(activityApplyService.CountApplicantNum(activity.getActivity_id()));
            /**participate人数*/
            activityDetailDto.setParticipant_num((activityParticipateService.CountParticipantNum(activity.getActivity_id())));


            /**该用户对活动的评分*/
            List<Map<String, Object>> activityMark = activityMarkService.GetUserMarktoAct(activity_id, user_id);

            /**活动的tag*/
            List<Map<String, Object>> activityTags = activityTagService.GetActAllTags(activity_id);

            res_map.put("activity_detail", activityDetailDto);
            res_map.put("activity_mark", activityMark);
            res_map.put("user_id", user_id);
            res_map.put("activity_tags", activityTags);


            return Result.success(res_map);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "getAct failed,活动id：" + activity_id + res_map);
        }

    }


    @ApiOperation("新建活动")
    @PostMapping("/postAct")
    public Result<Map<String, Object>> insertOneActivity(
            @RequestBody Activity activity,
            @ApiParam(name = "tag_ids", value = "tag_ids", required = true)
            @RequestParam("tag_ids") List<Long> tag_ids) {
        Map<String, Object> result_map = new HashMap<>();
        System.out.println(activity);
        System.out.println(tag_ids);
        Long newID;
        try {
            /**创建活动*/
            newID = activityService.CreateAct(activity);
            if (newID == Integer.MIN_VALUE + 1001) {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "insertOneActivity FAILED");
            }
            //创建人自动参与该活动
            ActivityParticipate activityParticipate = new ActivityParticipate(activity.getActivity_id(), activity.getCreator_id());
            activityParticipateService.AddParticipant(activityParticipate);

            /**创建关联tag*/
            activityTagService.AddActTags(activity.getActivity_id(), tag_ids);
//            System.out.println("创建关联tag");
//            for (Long tag_id:tag_ids){
//                System.out.println(tag_id);
//            }

        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "insertOneActivity FAILED");
        }
        result_map.put("activity_id", newID);
        return Result.success(result_map);
    }

    @ApiOperation("更新活动信息")
    @PatchMapping("/patchOneAct")
    public Result<String> patchOneAct(@RequestBody Activity activity,
                                      @ApiParam(name = "tag_ids", value = "tag_ids", required = true)
                                      @RequestParam("tag_ids") List<Long> tag_ids) {
        System.out.println("activity" + activity);
        System.out.println("tag_ids" + tag_ids);
        Long activity_id = activity.getActivity_id();
        try {
            /**更新activity表*/
            Integer resultCount = activityService.UpdateAct(activity);
            /**更新tag*/
            //删除所有旧tag
            activityTagService.DeleteActAllTags(activity_id);
            //添加当前tag
            activityTagService.AddActTags(activity_id, tag_ids);

            if (resultCount == 0) {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "patchOneAct FAILED");
            }
            return Result.success("patchOneAct SUCCESS");
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "patchOneAct FAILED");
        }

    }


    /**
     * 用户身份相关
     * */
    /**
     * 查询用户是不是这个活动的创建者
     */
    @ApiOperation("查询用户是不是这个活动的创建者")
    @GetMapping("/iscreator")
    public Result<ActivityUserRole> isCreator(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            return Result.success(activityService.isCreator(user_id, activity_id));
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "isCreator FAILED");
        }

    }

    /**
     * 查询用户是不是这个活动的参与者
     */
    @ApiOperation("查询用户是不是这个活动的报名者")
    @GetMapping("/isapplicant")
    public Result<ActivityUserRole> isApplicant(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            return Result.success(activityApplyService.isApplicant(user_id, activity_id));
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "isApplicant FAILED");
        }

    }

    /**
     * 用户报名活动
     */
    @ApiOperation("报名活动")
    @PostMapping("/postActApply")
    public Result<Integer> insertOneActApply(@RequestBody ActivityApply activityApply) {

        Integer res;
        try {
            res = activityApplyService.UserApplyAct(activityApply);
            if (res == Integer.MIN_VALUE + 1001) {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "insertOneActApply FAILED");
            }
            return Result.success(res);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "insertOneActApply FAILED");
        }
    }

    /**
     * 用户取消报名活动
     */
    @ApiOperation("用户取消报名活动")
    @DeleteMapping("/cancelApply")
    public Result<Integer> CancelApply(
            @ApiParam(name = "id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            Integer crud_delete = activityApplyService.UserCancelApplyAct(activity_id, user_id);
            if (crud_delete > 0) {
                return Result.success(crud_delete);
            } else {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "CancelApply FAILED");
            }
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "CancelApply FAILED");
        }

    }


    /**
     * 查询活动有多少人报名
     */
    @ApiOperation("查询活动有多少人报名")
    @GetMapping("/applicantNum")
    public Result<Integer> GetApplicantNum(
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            return Result.success(activityApplyService.CountApplicantNum(activity_id));
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "isCreator FAILED");
        }
    }

    /**
     * 活动创建人，终止报名，活动开始
     */
    @ApiOperation("活动创建人，终止报名")
    @PatchMapping("/creatorStopApply")
    public Result<String> creatorStopApply(
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
//        System.out.println("activity_id"+activity_id);
        try {
            //修改活动状态
            Integer res = activityService.SetActivityState(activity_id, 1);
            //取所有报名者
            List<ActivityApply> applyList = activityApplyService.GetAllApplicantList(activity_id);
            if (applyList.size() > 0) {
                //取得活动信息
                Activity activity = activityService.getAct(activity_id);
                Integer capacity = activity.getCapacity();
                Integer participatant_num = activityParticipateService.CountParticipantNum(activity_id);
                //按时间排序，最早在前
                List<ActivityApply> applyList_sort = applyList.stream().sorted
                        (Comparator.comparingLong(a -> a.getApply_time().getTime())).collect(Collectors.toList());
                System.out.println("applyList:" + applyList.toString());

                System.out.println("applyList_sort:" + applyList_sort.toString());

                //删除所有报名人
                activityApplyService.DeleteAct(activity_id);
                //按剩余空位，报名人依次进入参与名单
                for (int i = 0; i < capacity - participatant_num; i++) {
                    ActivityParticipate participate = new ActivityParticipate();
                    participate.setActivity_id(activity_id);
                    participate.setUser_id(applyList_sort.get(i).getUser_id());
                    activityParticipateService.AddParticipant(participate);
                }
            }


            return Result.success("creatorStopApply SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "creatorStopApply FAILED");

        }
    }


    /**
     * 活动创建人，活动结束，即进行中-》活动结束
     */
    @ApiOperation("活动创建人，活动结束")
    @PatchMapping("/creatorFinishAct")
    public Result<String> creatorFinishAct(
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
//        System.out.println("activity_id"+activity_id);
        try {
            Integer res = activityService.SetActivityState(activity_id, 2);
            return Result.success("creatorStopApply SUCCESS");
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "creatorStopApply FAILED");

        }
    }


    /**
     * 用户取消参与活动
     */
    @ApiOperation("用户取消参与活动")
    @DeleteMapping("/cancelParticipate")
    public Result<Integer> CancelParticipate(
            @RequestBody ActivityParticipate activityParticipate) {
        try {
            Integer crud_delete = activityParticipateService.RemoveParticipant(activityParticipate);
            if (crud_delete > 0) {
                return Result.success(crud_delete);
            } else {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "CancelParticipate FAILED");
            }
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "CancelParticipate FAILED");
        }

    }

    /**
     * 查询活动的参与人数
     */
    @ApiOperation("查询活动的参与人数")
    @GetMapping("/participantNum")
    public Result<Integer> GetParticipantNum(
            @ApiParam(name = "id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            return Result.success(activityParticipateService.CountParticipantNum(activity_id));
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "GetParticipantNum FAILED");
        }
    }

    /**
     * 获取参与某活动的所有用户列表
     */
    @ApiOperation("获取参与某活动的所有用户列表")
    @GetMapping("/getActApplicantList")
    public Result<List<Map<String, Object>>> GetActApplicantList(
            @ApiParam(name = "activity_id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            list = activityParticipateService.SelectActApplicantList(activity_id);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "GetActApplicantList FAILED");
        }
    }


    /**
     * 创建人删除活动
     * 同时删除其他所有表的相关记录
     */
    @ApiOperation("创建人删除活动")
    @DeleteMapping("/deleteAct")
    public Result<Map<String, Object>> DeleteAct(
            @ApiParam(name = "user_id", value = "用户id", required = true)
            @RequestParam("user_id") Long user_id,
            @ApiParam(name = "activity_id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        Integer delete_act;
        Map<String, Object> res_map = new HashMap<>();
        res_map.put("user_id", user_id);
        res_map.put("activity_id", activity_id);
        try {
            /**删除活动本身*/
            delete_act = activityService.DeleteAct(activity_id);
            System.out.println("delete_act:" + delete_act);
            if (delete_act == 1) {
                /**act本体删除成功，删除相关的表记录*/
                activityApplyService.DeleteAct(activity_id);
                activityParticipateService.DeleteAct(activity_id);
                return Result.success(res_map);
            } else {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "DeleteAct FAILED!");
            }
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "DeleteAct FAILED!");
        }

    }

    /**
     * 管理员删除活动
     * 同时删除其他所有表的相关记录
     * 发送通知
     */
    @ApiOperation("管理员删除活动")
    @DeleteMapping("/adminDeleteAct")
    public Result<Map<String, Object>> AdminDeleteAct(
            @ApiParam(name = "admin_id", value = "管理员id", required = true)
            @RequestParam("admin_id") Long admin_id,
            @ApiParam(name = "activity_id", value = "活动id", required = true)
            @RequestParam("activity_id") Long activity_id) {
        Integer delete_act;
        Map<String, Object> res_map = new HashMap<>();
        res_map.put("admin_id", admin_id);
        res_map.put("activity_id", activity_id);
        try {
            /**判断是管理员
             * */
            User admin=userService.findUser(admin_id);
            Activity activity=activityService.getAct(activity_id);
            Long creator_id=activity.getCreator_id();
            if (admin.getRole().equals("1")){

                /**删除活动本身*/
                delete_act = activityService.DeleteAct(activity_id);
                System.out.println("delete_act:" + delete_act);
                if (delete_act == 1) {
                    /**act本体删除成功，删除相关的表记录*/
                    activityApplyService.DeleteAct(activity_id);
                    activityParticipateService.DeleteAct(activity_id);

                    /**向创建者发送通知*/
                    systemNoticeUtil.SendSystemNotice(creator_id,"管理员删除活动","管理员"+admin.getName());

                    return Result.success(res_map);
                } else {
                    return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "DeleteAct FAILED!");
                }
            }
            else {
                return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "NOT ADMINISTRATOR !");

            }

        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "DeleteAct FAILED!");
        }

    }


    /**
     * 获取地图范围内的活动
     */
    @ApiOperation("获取地图范围内的活动")
    @GetMapping("/getMapAct")
    public Result<Map<String, Object>> GetMapAct(
            @ApiParam(name = "latitudes", value = "latitudes", required = true)
            @RequestParam("latitudes") List<Double> latitudes,
            @ApiParam(name = "longitudes", value = "longitudes", required = true)
            @RequestParam("longitudes") List<Double> longitudes
    ) {
//        System.out.println("latitudes"+latitudes);
        Map<String, Object> map = new HashMap<>();
        List<Activity> activityList = new ArrayList<>();
        try {
            activityList = activityService.GetMapActList(latitudes, longitudes);
            map.put("activityList", activityList);
            map.put("latitudes", latitudes);
            map.put("longitudes", longitudes);
            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "GetMapAct failed");
        }
    }


    @ApiOperation("处理好友的活动邀请")
    @PatchMapping("/handleFriendActInvite")
    public Result<Map<String,Object>> HandleFriendActInvite(
                                      @ApiParam(name = "activity_id", value = "activity_id", required = true)
                                      @RequestParam("activity_id")Long activity_id,
                                      @ApiParam(name = "user_id", value = "user_id", required = true)
                                      @RequestParam("user_id")Long user_id,
                                      @ApiParam(name = "friend_id", value = "friend_id", required = true)
                                      @RequestParam("friend_id")Long friend_id,
                                      @ApiParam(name = "handle_code", value = "handle_code", required = true)
                                      @RequestParam("handle_code")Integer handle_code
                                      ) {
        Map<String,Object> map=new HashMap<>();
        map.put("activity_id",activity_id);
        map.put("user_id",user_id);
        map.put("friend_id",friend_id);
        map.put("handle_code",handle_code);
        try{
            if (!friendRequestService.handleActivityInvite(user_id,friend_id,activity_id,handle_code)){
                return Result.fail(500,"handleFriendActInvite FAILD");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,e.getMessage());
        }

        return Result.success(map);

    }

    /**
     * @description: 管理员用：获取活动详情
     * @author: hym
     * @date: 2022/12/30 19:53
     * @param: \
     * @return:
     * @return: null
     **/
    @ApiOperation("管理员用：获取活动详情")
    @GetMapping("/adminGetActivityById")
    public Result<ActivityDetailDto> getActivityById(@ApiParam(name = "activity_id", value = "activity_id", required = true)
                                                         @RequestParam("activity_id") Long activity_id){
        try {
            /**获取Activity实体*/
            Activity activity = activityService.getAct(activity_id);

            /**组装活动detail的dto*/
            ActivityDetailDto activityDetailDto = new ActivityDetailDto(activity);
            /**apply人数*/
            activityDetailDto.setApplicant_num(activityApplyService.CountApplicantNum(activity.getActivity_id()));
            /**participate人数*/
            activityDetailDto.setParticipant_num((activityParticipateService.CountParticipantNum(activity.getActivity_id())));

            return Result.success(activityDetailDto);
        }
        catch (Exception e){
            return Result.fail(500,e.getMessage());
        }
    }
}
