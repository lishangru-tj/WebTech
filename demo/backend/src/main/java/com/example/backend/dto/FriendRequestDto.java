package com.example.backend.dto;

import com.example.backend.entity.User;
import com.example.backend.entity.friendRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendRequestDto {
    friendRequest friendRequest;
    String name;
    String avatar;
    String activity_name;
}
