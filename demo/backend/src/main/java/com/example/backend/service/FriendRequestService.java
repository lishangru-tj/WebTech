package com.example.backend.service;

import com.example.backend.dto.FriendRequestDto;
import com.example.backend.entity.friendRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FriendRequestService {
    List<FriendRequestDto> findFriendRequest(Long userid);
    boolean sendFriendRequest(Long userid,String friendEmail,Long activity_id,int status);
    boolean sendFriendRequestById(Long userid,Long friendId,Long activity_id,int status);
    boolean modifyFriendRequestStatus(Long userid,Long friendid);

    boolean handleActivityInvite(Long user_id,Long friend_id,Long activity_id,Integer handle_code);

    int deleteFriendRequest(Long friend_request_id);
}
