package com.example.backend.mapper;

import com.example.backend.entity.File;
import com.example.backend.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/3
 * @JDKVersion 17.0.4
 */
@Mapper
public interface FileMapper {
    @Select("SELECT * FROM file WHERE file_id=${fileId}")
    File selectFileInfo(@Param("fileId") Long fileId);
}
