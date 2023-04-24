package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.MessageBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/6
 * @JDKVersion 17.0.4
 */
@Mapper
public interface MessageBoardMapper extends BaseMapper<MessageBoard> {
    @Select("SELECT * FROM message_board WHERE activity_id=${activityId}")
    List<MessageBoard> selectActivityAllMessageBoard(@Param("activityId") Long activityId);

    @Insert("INSERT into message_board (message_board_id,activity_id,user_id,content,create_time) values(#{messageBoardId},#{activityId},#{userId},#{content},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "messageBoardId", keyColumn = "message_board_id")
    int insertMessageBoard(MessageBoard messageBoard);
}
