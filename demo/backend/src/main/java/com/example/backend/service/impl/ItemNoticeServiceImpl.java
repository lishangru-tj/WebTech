package com.example.backend.service.impl;

import com.example.backend.entity.ItemNotice;
import com.example.backend.mapper.ItemNoticeMapper;
import com.example.backend.service.ItemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/11/28
 * @JDKVersion 17.0.4
 */
@Service
public class ItemNoticeServiceImpl implements ItemNoticeService {
    @Autowired
    ItemNoticeMapper itemNoticeMapper;

    @Override
    public int addItemNotice(ItemNotice itemNotice) {
        return itemNoticeMapper.insertItemNotice(itemNotice);
    }

    @Override
    public List<ItemNotice> findItemNotice(Long userId) {
        return itemNoticeMapper.selectItemNotice(userId);
    }

    @Override
    public int modifyItemNoticeStatus(Long itemNoticeId) {
        return itemNoticeMapper.modifyItemNoticeStatus(itemNoticeId);
    }

    @Override
    public Integer deleteItemNotice(Long item_notice_id) {

        return itemNoticeMapper.deleteById(item_notice_id);
    }
}
