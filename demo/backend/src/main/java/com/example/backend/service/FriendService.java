package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.dto.FriendDTO;
import com.example.backend.entity.User;
import com.example.backend.entity.friend;
import com.example.backend.entity.friendGroup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FriendService {
    Result<String> addFriend(Long userid,Long friendid);
    List<Map<String,Object>> getFriends(Long id);
    List<friend> getFriendList(Long id);
    boolean setNewFriends(FriendDTO friendDTO);
    List<friendGroup>getFriendGroups(Long id);
    String deleteFriend(Long userId,Long deleteId,Long groupId);
}
