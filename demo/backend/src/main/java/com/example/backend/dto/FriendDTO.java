package com.example.backend.dto;

import com.example.backend.entity.friend;
import com.example.backend.entity.friendGroup;
import lombok.Data;

import java.util.List;

@Data
public class FriendDTO {
    private Long id;
    private List<friend>friendList;
    private List<friendGroup>friendGroup;
}
