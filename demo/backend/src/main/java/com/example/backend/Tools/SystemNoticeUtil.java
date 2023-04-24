package com.example.backend.Tools;

import com.example.backend.entity.Notice;
import com.example.backend.entity.UserNotice;
import com.example.backend.service.NoticeService;
import com.example.backend.service.UserNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.backend.Tools.SendNoticeUtil.formatDay;

/**
 * @Author: 杨严
 * @Date: 2022/12/19/6:55 PM
 * @Description: 发送系统通知的工具类
 */
@Component
public class SystemNoticeUtil {
    @Autowired
    NoticeService noticeService;
    @Autowired
    UserNoticeService userNoticeService;


    /**
     * @param 用户id,通知标题,通知内容
     * 发送系统通知
     * */
    public Integer SendSystemNotice(Long user_id,String title,String content)
    {
        Integer i;
        // 获取当前的时间
        var Now = new Date();
        Notice notice = new Notice(formatDay(Now)+":"+title, content, Now, 1);
        noticeService.addNotice(notice);
        UserNotice userNotice = new UserNotice(user_id, notice.getNoticeId(), 0);
        i=userNoticeService.addUserNotice(userNotice);
        return i;
    }
}
