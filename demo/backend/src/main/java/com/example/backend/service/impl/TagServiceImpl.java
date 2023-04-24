package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Tag;
import com.example.backend.mapper.TagMapper;
import com.example.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/01/7:21 PM
 * @Description:
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Override
    public Integer DeleteTag(Long tag_id) {
        return tagMapper.deleteById(tag_id);
    }

    @Override
    public Long InsertTag(Tag tag) {
        Long newID=Long.valueOf(tagMapper.insert(tag));
        return newID;
    }

    @Override
    public List<Map<String,Object>> GetAllTags() {
        return tagMapper.SelectAll();
    }
    public Tag findTag(Long tag_id){
        Tag tag=tagMapper.selectById(tag_id);
        System.out.println(tag);
        return tag;
    }

}
