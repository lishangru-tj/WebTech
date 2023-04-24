package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.friendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface FriendRequestMapper extends BaseMapper<friendRequest> {

    @Select(
            "SELECT * "+
                    "FROM friend_request "+
                    "WHERE activity_id = ${activity_id} and "+
                    " friendid = ${friend_id} and "+
                    " userid = ${user_id} and "+
                    " status = ${status} "
    )
    List<Map<String,Object>> SelectFriendRequestActInviteList(@Param("activity_id")Long activity_id,
                                                              @Param("friend_id")Long friend_id,
                                                              @Param("user_id")Long user_id,
                                                              @Param("status")int status);
}
