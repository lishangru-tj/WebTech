package com.example.backend.service.impl;

import com.example.backend.common.Result;
import com.example.backend.dto.UserForgetRequestDTO;
import com.example.backend.entity.User;
import com.example.backend.entity.UserForgetRequest;
import com.example.backend.mapper.UserForgetRequestMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.UserForgetRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class UserForgetRequestServiceImpl implements UserForgetRequestService {
    @Autowired
    private UserForgetRequestMapper userForgetRequestMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    JavaMailSenderImpl mailSender;
    public Result<UserForgetRequest> forgetRequest(UserForgetRequest userForgetRequest){

        User user=userMapper.selectByEmail(userForgetRequest.getId());
        if (user==null){
            return Result.fail(10001,"用户不存在!");
        }
        UserForgetRequest re;
        String code="";
        for (int i=0;i<4;i++){
            code =  code + String.valueOf((int) Math.floor(Math.random() * 9 + 1));
        }
        re=new UserForgetRequest(userForgetRequest.getId(),code);
        if (userForgetRequestMapper.selectById(userForgetRequest.getId())==null){
            userForgetRequestMapper.insert(re);

        }
        else {
            userForgetRequestMapper.updateById(re);
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            helper.setSubject("济时:请验证您的邮箱");

            helper.setText("尊敬的" + user.getName() + "您好！\n"
                    + "您正在尝试重置您的密码，请输入下面的验证码来进行验证\n"+
                    "\t\t\t"+code);
            helper.setTo(user.getEmail());
            helper.setFrom("417695971@qq.com");
        } catch (MessagingException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
        return Result.success(re);

    }
    public Result<String> resetPwd(UserForgetRequestDTO userForgetRequestDTO){
        UserForgetRequest userForgetRequest;
        userForgetRequest=userForgetRequestMapper.selectById(userForgetRequestDTO.getId());
        if(userForgetRequest==null){
            return Result.fail(10001,"你好像偷改了邮箱?");
        }
        if(!userForgetRequestDTO.getCode().equals(userForgetRequest.getCode())){
            return Result.fail(10001,"验证码不对！也许是失效了?");
        }
        if(userMapper.updateUserPwd(DigestUtils.md5DigestAsHex(userForgetRequestDTO.getPwd().getBytes()), userForgetRequestDTO.getId())>0){
            return Result.success("成功修改！");
        }
            return Result.fail(10001,"奇怪的错误");
    }
}
