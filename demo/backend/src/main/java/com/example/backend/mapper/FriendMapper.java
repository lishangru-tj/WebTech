package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.common.Result;
import com.example.backend.entity.friend;
import lombok.Value;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendMapper extends BaseMapper<friend> {
    @Select("select friend.groupid,friend.name,user.name as rootname,user.email,user.avatar,user.age,user.introduce,user.id from friend,user where user.id=friend.friendid and friend.userid='${userid}'")
    List<Map<String,Object>> selectFriend(@Param("userid")Long userid);
}
