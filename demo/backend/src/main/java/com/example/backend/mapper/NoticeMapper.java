package com.example.backend.mapper;

import com.example.backend.entity.Message;
import com.example.backend.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Mapper
public interface NoticeMapper {
    @Select("SELECT * FROM notice WHERE notice_id=${noticeId}")
    Notice selectNoticeById(@Param("noticeId") Long noticeId);

    @Insert("INSERT into notice (notice_id,activity_id,title,content,create_time,type) values(#{noticeId},#{activityId},#{title},#{content},#{createTime},#{type})")
    @Options(useGeneratedKeys = true, keyProperty = "noticeId", keyColumn = "notice_id")
    int insertNotice(Notice notice);
}
