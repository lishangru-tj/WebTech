package com.example.backend.service;


import com.example.backend.common.Result;
import com.example.backend.entity.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @Author hym
 * @Date 2022/10/29
 * @Description demo
 */
@Service
public interface UserService {
    User findUser(Long id);

    User getUserByEmail(String email);
    Result<String> confirmUser(String email, String password);
    Result<String> registerUser(String email,String password,String name);
    Result<User> putUser(Long id,String name,String introduce,int age,String imgUrl);
    Result<String> email(User user);

    List<User> findAllUser();

    }
