package com.example.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author hym
 * @Date 2022/10/29
 * @Description demo
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where email = '${email}' ")
    User selectByEmail(@Param("email") String email);
    @Insert("insert into user (name,email,password) VALUES ('${name}','${email}','${password}')")
    int insertUser(@Param("name") String name,@Param("email") String email,@Param("password")String password);
    @Update("UPDATE user SET name='${name}',age='${age}',introduce='${introduce}',avatar='${imgUrl}' WHERE id='${id}'")
    int updateUser(@Param("id")Long id,@Param("name")String name,@Param("age")int age,@Param("introduce")String introduce,@Param("imgUrl")String imgUrl);


    @Select("select * from user")
    List<User> selectAllUser();
    
    @Update("UPDATE user SET password='${pwd}' WHERE email='${email}'")
    int updateUserPwd(@Param("pwd")String pwd,@Param("email") String email);
}
