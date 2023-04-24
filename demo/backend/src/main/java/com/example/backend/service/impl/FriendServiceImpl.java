package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.common.Result;
import com.example.backend.dto.FriendDTO;
import com.example.backend.entity.User;
import com.example.backend.entity.friend;
import com.example.backend.entity.friendGroup;
import com.example.backend.mapper.FriendGroupMapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    FriendMapper friendMapper;
    @Autowired
    FriendGroupMapper friendGroupMapper;
    public Result<String> addFriend(Long userid,Long friendid){
        try {
            friendMapper.insert(new friend(userid, friendid, 1L, null));
            friendMapper.insert(new friend(friendid, userid, 1L, null));
        }catch (Exception e){
            return Result.fail(10001,e.getMessage());
        }
        return Result.success("添加成功");
    }
    public List<Map<String,Object>> getFriends(Long id){
        var friends=friendMapper.selectFriend(id);
        return friends;
    }
    public List<friend> getFriendList(Long id){
        QueryWrapper<friend> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userid",id);
        var friends=friendMapper.selectList(queryWrapper);
        return friends;
    }
    public List<friendGroup> getFriendGroups(Long id){
        QueryWrapper<friendGroup> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("belongid",id);
        var friendgroups= friendGroupMapper.selectList(queryWrapper);
        return friendgroups;
    }
    public boolean setNewFriends(FriendDTO friendDTO){
        Map<String, Object> friendGroupMap = new HashMap<>();
        friendGroupMap.put("belongid", friendDTO.getId());
        friendGroupMapper.deleteByMap(friendGroupMap);
        friendDTO.getFriendGroup().forEach(friendGroup -> {
            friendGroupMapper.insert(friendGroup);
        });
        Map<String, Object> friendMap = new HashMap<>();
        friendMap.put("userid", friendDTO.getId());
        friendMapper.deleteByMap(friendMap);
        friendDTO.getFriendList().forEach(friend -> {
            friendMapper.insert(friend);
        });
        return true;
    }
    public String deleteFriend(Long userId,Long deleteId,Long groupId){
        QueryWrapper<friend> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userid",userId);
        queryWrapper.eq("friendid",deleteId);
        queryWrapper.eq("groupid",groupId);
        if(friendMapper.delete(queryWrapper)>0)
            return "删除成功";
        else
            return"删除失败";
    }
}
