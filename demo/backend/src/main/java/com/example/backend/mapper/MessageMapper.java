package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/24
 * @JDKVersion 17.0.4
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message WHERE activity_id=${activityId}")
    List<Message> selectActivityAllMessage(@Param("activityId") Long activityId);

    @Insert("INSERT into message (message_id,activity_id,sender_id,status,type,file_id,send_time,content) values(#{messageId},#{activityId},#{senderId},#{status},#{type},#{fileId},#{sendTime},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId", keyColumn = "message_id")
    int insertMessage(Message message);
}
