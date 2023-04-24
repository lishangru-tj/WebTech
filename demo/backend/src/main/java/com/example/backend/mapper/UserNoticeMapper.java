package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.UserNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Mapper
public interface UserNoticeMapper extends BaseMapper<UserNotice> {
    @Select("SELECT * FROM user_notice WHERE user_id=${userId}")
    List<UserNotice> selectOneUserAllNotice(@Param("userId") Long userId);

    @Update("UPDATE user_notice set status=1 WHERE user_id=${userId} AND notice_id=${noticeId}")
    int updateNoticeStatus(@Param("userId") Long userId,@Param("noticeId")Long noticeId);

    @Update("INSERT into user_notice (user_id,notice_id,status) values(#{userId},#{noticeId},#{status})")
    int insertUserNotice(UserNotice userNotice);
}
