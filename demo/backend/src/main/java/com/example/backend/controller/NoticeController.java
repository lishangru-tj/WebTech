package com.example.backend.controller;

import com.example.backend.Tools.SystemNoticeUtil;
import com.example.backend.dto.ActivityNotice;
import com.example.backend.dto.FriendRequestDto;
import com.example.backend.dto.SystemNotice;
import com.example.backend.common.Result;
import com.example.backend.entity.*;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.spi.DirStateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Api(tags = {"Notice"})
@RestController
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    UserNoticeService userNoticeService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    ItemNoticeService itemNoticeService;

    @Autowired
    FriendRequestService friendRequestService;
    @Autowired
    FriendService friendService;

    @Autowired
    SystemNoticeUtil systemNoticeUtil;

    @Autowired
    UserMapper userMapper;
    @ApiOperation("获取该用户所有系统通知信息")
    @GetMapping("getSystemNoticeByUserID")
    public Result<List<SystemNotice>> findOneUserSystemNoticeInfo(@ApiParam(name = "userId", value = "要查找的用户名", required = true)
                                                                  @RequestParam("userId") Long userId) {
        try {
            ArrayList<SystemNotice> systemNotices = new ArrayList<SystemNotice>();
            List<UserNotice> oneUserAllNotice = userNoticeService.findOneUserAllNotice(userId);
            for (UserNotice un : oneUserAllNotice) {
                Notice noticeInfo = noticeService.getNoticeById(un.getNoticeId());
                if (noticeInfo.getType() == 1) {
                    SystemNotice systemNotice = new SystemNotice(noticeInfo.getNoticeId(), noticeInfo.getTitle(), noticeInfo.getContent(), noticeInfo.getCreateTime(), un.getStatus());
                    systemNotices.add(systemNotice);
                }
            }
            return Result.success(systemNotices);

        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }
    //好友通知
    @ApiOperation("获取该用户所有好友申请")
    @GetMapping("getFriendRequest")
    public Result<List<FriendRequestDto>> findOneUserFriendRequest(@ApiParam(name="userId",value = "要查找的用户名",required = true)
                                                                @RequestParam("userId") Long userId){
        try{
            var re=friendRequestService.findFriendRequest(userId);
            return Result.success(re);
        }catch (Exception e){
            return Result.fail(500,e.getMessage());
        }
    }
    //结束

    @ApiOperation("获取该用户所有活动通知")
    @GetMapping("getActivityNoticeByUserID")
    public Result<List<ActivityNotice>> findOneUserActivityNoticeInfo(@ApiParam(name = "userId", value = "要查找的用户名", required = true)
                                                                      @RequestParam("userId") Long userId) {
        try {
            ArrayList<ActivityNotice> activityNotices = new ArrayList<ActivityNotice>();
            List<UserNotice> oneUserAllNotice = userNoticeService.findOneUserAllNotice(userId);
            for (UserNotice un : oneUserAllNotice) {
                Notice noticeInfo = noticeService.getNoticeById(un.getNoticeId());
                if (noticeInfo.getType() == 2) {
                    ActivityNotice activityNotice = new ActivityNotice(noticeInfo.getNoticeId(), noticeInfo.getActivityId(), noticeInfo.getTitle(), noticeInfo.getContent(), noticeInfo.getCreateTime(), un.getStatus());
                    activityNotices.add(activityNotice);
                }
            }
            return Result.success(activityNotices);

        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }

    @ApiOperation("获取该用户所有个人事项通知")
    @GetMapping("getItemNoticeByUserID")
    public Result<List<ItemNotice>> findItemNotice(@ApiParam(name = "userId", value = "用户ID", required = true)
                                                   @RequestParam("userId") Long userId) {
        try {
            List<ItemNotice> itemNotice = itemNoticeService.findItemNotice(userId);
            return Result.success(itemNotice);

        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }


    @ApiOperation("更新通知的已读状态")
    @PatchMapping("modifyNoticeStatus")
    public Result<String> modifyNoticeStatus(@ApiParam(name = "userId", value = "用户ID", required = true)
                                             @RequestParam("userId") Long userId,
                                             @ApiParam(name = "noticeId", value = "通知ID", required = true)
                                             @RequestParam("noticeId") Long noticeId) {
        try {
            int res = userNoticeService.modifyNoticeStatus(userId, noticeId);
            if (res == 1) {
                return Result.success("消息状态更新成功！");
            } else {
                return Result.success("消息状态更新失败！");
            }
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }

    @ApiOperation("更新个人事项通知的已读状态")
    @PatchMapping("modifyItemNoticeStatus")
    public Result<String> modifyItemNoticeStatus(@ApiParam(name = "itemNoticeId", value = "事项通知ID", required = true)
                                                 @RequestParam("itemNoticeId") Long itemNoticeId) {
        try {
            int res = itemNoticeService.modifyItemNoticeStatus(itemNoticeId);
            if (res == 1) {
                return Result.success("消息状态更新成功！");
            } else {
                return Result.success("消息状态更新失败！");
            }
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }
    @ApiOperation("拒绝好友申请")
    @PatchMapping("rejectFriendRequest")
    public Result<String> rejectFriendRequest(@ApiParam(name = "userid", value = "好友通知所属人id", required = true)
                                                        @RequestParam("userid") Long userid,
                                                    @ApiParam(name = "friendid", value = "对方id", required = true)
                                                    @RequestParam("friendid") Long friendid){
        try{
            if (friendRequestService.modifyFriendRequestStatus(userid,friendid)){
                friendRequestService.sendFriendRequestById(friendid,userid,null,2);

                User friend =userMapper.selectById(userid);
                String title="好友申请被拒绝";
                String content=friend.getName()+" 拒绝了您的好友申请";
                systemNoticeUtil.SendSystemNotice(friendid,title,content);

                return Result.success("信息状态更新成功");
            }
            return Result.success("更新失败");
        }catch (Exception e){
            return Result.fail(500, e.getMessage());
        }
    }
    @ApiOperation("同意好友申请")
    @PatchMapping("acceptFriendRequest")
    public Result<String> acceptFriendRequest(@ApiParam(name = "userid", value = "好友通知所属人id", required = true)
                                                  @RequestParam("userid") Long userid,
                                              @ApiParam(name = "friendid", value = "对方id", required = true)
                                                  @RequestParam("friendid") Long friendid){

        try{
            if (friendRequestService.modifyFriendRequestStatus(userid,friendid)){
                friendRequestService.sendFriendRequestById(friendid,userid,null,3);
                friendService.addFriend(userid,friendid);

                User friend =userMapper.selectById(userid);
                String title="好友申请通过";
                String content=friend.getName()+" 同意了您的好友申请";
                systemNoticeUtil.SendSystemNotice(friendid,title,content);

                return Result.success("信息状态更新成功");
            }
            return Result.success("更新失败");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Result.fail(500, e.getMessage());
        }
    }


    @ApiOperation("删除user notice")
    @DeleteMapping("deleteUserNotice")
    public Result<Map<String, Object>> deleteUserNotice(
            @ApiParam(name = "user_id", value = "user_id", required = true)
            @RequestParam() Long user_id,
            @ApiParam(name = "notice_id", value = "notice_id", required = true)
            @RequestParam() Long notice_id
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("notice_id",notice_id);
        map.put("user_id",user_id);
        try {
            Integer i=userNoticeService.deleteUserNotice(user_id,notice_id);
            map.put("i",i);

            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    @ApiOperation("删除item notice")
    @DeleteMapping("deleteItemNotice")
    public Result<Map<String, Object>> deleteItemNotice(
            @ApiParam(name = "item_notice_id", value = "item_notice_id", required = true)
            @RequestParam() Long item_notice_id

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("item_notice_id",item_notice_id);
        try {
            Integer i=itemNoticeService.deleteItemNotice(item_notice_id);
            map.put("i",i);

            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    @ApiOperation("删除friend request")
    @DeleteMapping("deleteFriendRequest")
    public Result<Map<String, Object>> deleteFriendRequest(
            @ApiParam(name = "friend_request_id", value = "friend_request_id", required = true)
            @RequestParam() Long friend_request_id

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("friend_request_id",friend_request_id);
        try {
            Integer i=friendRequestService.deleteFriendRequest(friend_request_id);
            map.put("i",i);

            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }
}
