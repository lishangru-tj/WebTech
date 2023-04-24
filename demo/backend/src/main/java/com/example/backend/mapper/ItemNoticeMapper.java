package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ItemNotice;
import com.example.backend.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Mapper
public interface ItemNoticeMapper extends BaseMapper<ItemNotice> {
    @Select("SELECT * FROM item_notice WHERE user_id=${userId}")
    List<ItemNotice> selectItemNotice(@Param("userId") Long userId);

    @Update("UPDATE item_notice set status=1 WHERE item_notice_id=${itemNoticeId}")
    int modifyItemNoticeStatus(@Param("itemNoticeId")Long itemNoticeId);

    @Insert("INSERT into item_notice (item_notice_id,user_id,title,content,create_time,item_id,status) values(#{itemNoticeId},#{userId},#{title},#{content},#{createTime},#{itemId},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "itemNoticeId", keyColumn = "item_notice_id")
    int insertItemNotice(ItemNotice itemNotice);
}
