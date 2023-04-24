package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.backend.Tools.SystemNoticeUtil;
import com.example.backend.dto.ActivityUserRole;
import com.example.backend.dto.FriendRequestDto;
import com.example.backend.entity.Activity;
import com.example.backend.entity.ActivityParticipate;
import com.example.backend.entity.User;
import com.example.backend.entity.friendRequest;
import com.example.backend.mapper.ActivityMapper;
import com.example.backend.mapper.FriendRequestMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.ActivityApplyService;
import com.example.backend.service.ActivityParticipateService;
import com.example.backend.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FriendRequestImpl implements FriendRequestService {
    @Autowired
    FriendRequestMapper friendRequestMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ActivityApplyService activityApplyService;

    @Autowired
    ActivityParticipateService activityParticipateService;

    @Autowired
    SystemNoticeUtil systemNoticeUtil;

    @Override
    public List<FriendRequestDto> findFriendRequest(Long userid) {
        QueryWrapper<friendRequest> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userid",userid);
        List<FriendRequestDto> re=new ArrayList<>();
        var requestList=friendRequestMapper.selectList(queryWrapper);
        for (int i=0;i<requestList.size();i++){
            User user=userMapper.selectById(requestList.get(i).getFriendid());
            System.out.println("获取活动id"+requestList.get(i));
            Activity activity=activityMapper.selectById(requestList.get(i).getActivityId());

            if (activity!=null) {
                re.add(new FriendRequestDto(requestList.get(i), user.getName(), user.getAvatar(), activity.getTitle_name()));
            }
            else
                re.add(new FriendRequestDto(requestList.get(i), user.getName(), user.getAvatar(),null));
        }
        return re;
    }

    @Override
    public boolean sendFriendRequest(Long userid, String friendEmail,Long activity_id,int status) {
        var Now=new Date();
        Long friendId=userMapper.selectByEmail(friendEmail).getId();
        friendRequest friend_request=new friendRequest(null,friendId,userid,Now,activity_id,status);
        List<Map<String, Object>> tList;
        //查找是不是已经发送过了
        if (activity_id!=null) {
            tList = friendRequestMapper.SelectFriendRequestActInviteList(activity_id, friendId, userid, status);
        }
        else {
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("userid",friendId);
            queryWrapper.eq("friendid",userid);
            tList=friendRequestMapper.selectList(queryWrapper);
        }
        if (tList.size()>0){
            return  true;
        }
        try {
            if (friendRequestMapper.insert(friend_request) > 0) {
                return true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public boolean sendFriendRequestById(Long userid, Long friendid,Long activity_id,int status) {
        QueryWrapper<friendRequest> queryWrapper=new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("status",status);
        if (activity_id!=null)
            queryWrapper.eq("activity_id",activity_id);
        else
            queryWrapper.eq("friendid",friendid);
        var Now=new Date();
        friendRequest friend_request=new friendRequest(null,userid,friendid,Now,activity_id,status);
        //查找是不是已经发送过了
        List<friendRequest> tList=friendRequestMapper.selectList(queryWrapper);

        if (tList.size()>0){
            return  true;
        }
        if(friendRequestMapper.insert(friend_request)>0) {
            return true;
        }
        return false;
    }
    @Override
    public boolean modifyFriendRequestStatus(Long userid,Long friendid){
        if(friendRequestMapper.update(null, Wrappers.<friendRequest>lambdaUpdate()
                .set(friendRequest::getStatus,0)
                .eq(friendRequest::getUserid,userid)
                .eq(friendRequest::getFriendid,friendid))>0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean handleActivityInvite(Long user_id, Long friend_id, Long activity_id, Integer handle_code) {
        boolean flag=false;
        User friend=userMapper.selectById(user_id);
        Activity activity=activityMapper.selectById(activity_id);
        if (handle_code.equals(0)){
            /**handle code 0 拒绝邀请*/

            /**通知发送者 */
            String title=friend.getName()+" 拒绝了您的活动邀请";

            String content="您的好友 "+friend.getName()
                    +"拒绝您发送的活动参与邀请，拒绝参与活动 "+activity.getTitle_name()+" ";
            systemNoticeUtil.SendSystemNotice(friend_id,title,content);
            flag=true;
        } else if (handle_code.equals(1)) {
            /**handle code 1 接受邀请*/



            /**判断user是不是活动的报名者*/
            ActivityUserRole RoleApplicant=activityApplyService.isApplicant(user_id,activity_id);
            boolean isApplicant=RoleApplicant.getIs_applicant();
            if (isApplicant){
                /**是报名者则从报名者中删除*/
                activityApplyService.UserCancelApplyAct(activity_id,user_id);
            }
            /**user 是不是活动的参与者*/
            ActivityUserRole RoleParticipant=activityParticipateService.isMember(user_id,activity_id);
            if (!RoleParticipant.getIs_member()){
                /**user添加到活动的参与者*/
                ActivityParticipate activityParticipate=new ActivityParticipate(activity_id,user_id);
                activityParticipateService.AddParticipant(activityParticipate);
            }

            /**通知发送者 */
            String title=friend.getName()+" 同意了您的活动邀请";

            String content="您的好友 "+friend.getName()
                    +"同意您发送的活动参与邀请，已参与活动 "+activity.getTitle_name()+" ";
            systemNoticeUtil.SendSystemNotice(friend_id,title,content);
            flag=true;
        }
        //更新消息状态
        if(flag){
            friendRequestMapper.update(null, Wrappers.<friendRequest>lambdaUpdate()
                    .eq(friendRequest::getUserid,user_id)
                    .eq(friendRequest::getActivityId,activity_id)
                    .eq(friendRequest::getStatus,4)
                    .eq(friendRequest::getFriendid,friend_id)
                    .set(friendRequest::getStatus,5));
        }

        return flag;
    }

    @Override
    public int deleteFriendRequest(Long friend_request_id) {
        return friendRequestMapper.deleteById(friend_request_id);
    }
}
