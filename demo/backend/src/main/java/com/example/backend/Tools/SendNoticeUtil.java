package com.example.backend.Tools;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/6
 * @JDKVersion 17.0.4
 */

import com.example.backend.controller.ChatController;
import com.example.backend.dto.FormatMessage;
import com.example.backend.dto.FormatUser;
import com.example.backend.dto.LiveWeatherDto;
import com.example.backend.entity.*;
import com.example.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 定时任务 静态定时任务
 * <p>
 * 第一位，表示秒，取值0-59
 * 第二位，表示分，取值0-59
 * 第三位，表示小时，取值0-23
 * 第四位，日期天/日，取值1-31
 * 第五位，日期月份，取值1-12
 * 第六位，星期，取值1-7，1表示星期天，2表示星期一
 * 第七位，年份，可以留空，取值1970-2099
 *
 * @author crush
 * @Date: 2021-07-27 21:13
 * @since 1.0.0
 */
@Component
public class SendNoticeUtil {
    @Autowired
    TaskExpireWebSocket taskExpireWebSocket;

    @Autowired
    ActivityParticipateService activityParticipateService;

    @Autowired
    ActivityService activityService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserNoticeService userNoticeService;

    @Autowired
    ItemNoticeService itemNoticeService;

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static String formatDay(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        return fmt.format(date);
    }

    public static long timeDayDiff(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day;
    }

    public static long timeHourDiff(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        long hour = diff % nd / nh;
        return hour;
    }

    public static long timeMinuteDiff(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        return min;
    }


    // 每天早上八点触发
    @Scheduled(cron = "0 0 8 * * ? ")
    private void checkActivityDaily() throws IOException {
        List<User> allUser = userService.findAllUser();
        for (User user : allUser) {
            // 获取当前的时间
            var Now = new Date();
            int activity_num = 0;
            // 根据用户ID获取到其所有的活动
            List<Long> activityIds = activityParticipateService.FindOneUserAllActivityId(user.getId());
            for (Long Ids : activityIds) {
                Activity actInfo = activityService.getAct(Ids);
                // 判断活动的开始时间是否为今天
                if (isSameDay(Now, actInfo.getStart_time())) {
                    activity_num += 1;
                }
            }
            if (activity_num > 0) {
                System.out.println("发送活动提醒消息");

                //获取今天一整天的预测天气
                String weatherText = GaodeUtil.getForecastWeatherText();
                Notice notice = new Notice(formatDay(Now) + "活动提醒", "您今天有" + Integer.toString(activity_num) + "项要参加的活动哦，请注意及时参加" + "\n" + weatherText, Now, 1);

                noticeService.addNotice(notice);
                UserNotice userNotice = new UserNotice(user.getId(), notice.getNoticeId(), 0);
                userNoticeService.addUserNotice(userNotice);
            }
        }
    }

    // 每天早上八点触发
    @Scheduled(cron = "0 0 8 * * ? ")
    private void checkTaskDaily() {
        List<User> allUser = userService.findAllUser();
        for (User user : allUser) {
            // 获取当前的时间
            var Now = new Date();
            int task_num = 0;
            // 根据用户ID获取到其所有的事项
            List<Task> allTaskAndRelative = taskService.findAllTaskAndRelative(user.getId());
            for (Task task : allTaskAndRelative) {
                if (isSameDay(Now, task.getStartTime())) {
                    task_num += 1;
                }
            }
            if (task_num > 0) {
                System.out.println("发送事项通知");
                Notice notice = new Notice(formatDay(Now) + "事项提醒", "您今天有" + Integer.toString(task_num) + "项要完成的事项哦，请注意及时参加", Now, 1);
                noticeService.addNotice(notice);
                UserNotice userNotice = new UserNotice(user.getId(), notice.getNoticeId(), 0);
                userNoticeService.addUserNotice(userNotice);
            }
        }
    }

    //每5秒钟执行一次，看看前端目前的事项状态有没有失效
    @Scheduled(cron = "*/5 * * * * ?")
    private void checkTaskExpire(){
        //获取当前时间
        Timestamp currentTimestamp = DateTimeUtil.getCurrentTimestamp();

        //获取所有事项
        List<Task> allTaskList = taskService.findAllTask();
        for(Task t : allTaskList)
        {
            //检测前端状态转变：未开始->进行中
            if(t.getIsRemind() < 3 && t.getStartTime().before(DateTimeUtil.getCurrentTimestamp()))
            {
                Message message = new Message(t.getTaskId(),t.getUserId(),t.getTaskState(),0,null,"未开始->进行中");
                System.out.println("aaaaaaaaaaaaaa"+message);
                FormatMessage formatMessage = formatMessage(message);
                boolean isSend = taskExpireWebSocket.sendMessage(t.getTaskId().toString(), formatMessage);

                //isRemind++
                if(isSend) {
                    int isRemind = t.getIsRemind();
                    t.setIsRemind(isRemind + 1);
                    taskService.patchOneTask(t);
                }
            }

            //检测前端状态转变：进行中->延期中
            else if(t.getIsRemind() < 4 && t.getEndTime().before(DateTimeUtil.getCurrentTimestamp()))
            {
                Message message = new Message(t.getTaskId(),t.getUserId(),t.getTaskState(),0,null,"进行中->延期中");
                FormatMessage formatMessage = formatMessage(message);
                boolean isSend = taskExpireWebSocket.sendMessage(t.getTaskId().toString(), formatMessage);

                //isRemind++
                if(isSend) {
                    int isRemind = t.getIsRemind();
                    t.setIsRemind(isRemind + 1);
                    taskService.patchOneTask(t);
                }
            }

        }
    }

    // 每5秒钟执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    private void checkActivityTime() throws IOException {
        // 获取当前的时间
        var Now = new Date();
        // 获取所有活动
        List<Activity> allActivity = activityService.getAll();
        for (Activity activity : allActivity) {
            var s_time = activity.getStart_time();
            // 当前活动还没开始
            if (s_time.getTime() > Now.getTime()) {
                /*生成天气报表，因为马上要出门了，所以要根据实时天气生成提醒*/
                /*调用天气API*/
                String weatherText = GaodeUtil.getLiveWeatherText();

                var isRemind = activity.getIsRemind();
                if (timeMinuteDiff(s_time, Now) <= 10 && (isRemind == 0 || isRemind == 1)) {
                    System.out.println("活动还有十分钟开始" + activity.getActivity_id() + "\n" + weatherText);
                    // 生成消息
                    Notice notice = new Notice(activity.getActivity_id(), activity.getTitle_name() + "活动开始提醒", "该活动还有十分钟就要开始了，请注意及时参加哦" + "\n" + weatherText, Now, 2);
                    // 添加活动提醒消息
                    noticeService.addNotice(notice);
                    List<Map<String, Object>> userMaps = activityParticipateService.SelectActApplicantList(activity.getActivity_id());
                    // 将信息发送给活动中所有的用户
                    for (Map<String, Object> user : userMaps) {
                        UserNotice userNotice = new UserNotice((Long) user.get("user_id"), notice.getNoticeId(), 0);
                        userNoticeService.addUserNotice(userNotice);
                    }
                    // 更新活动提醒状态
                    activityService.modifyActivityRemind(activity.getActivity_id(), 2);
                } else if (timeHourDiff(s_time, Now) <= 1 && isRemind == 0) {
                    System.out.println("活动还有一个小时开始" + activity.getActivity_id());
                    // 生成消息
                    Notice notice = new Notice(activity.getActivity_id(), activity.getTitle_name() + "活动开始提醒", "该活动还有一小时就要开始了，请注意及时参加哦" + "\n" + weatherText, Now, 2);
                    // 添加活动提醒消息
                    noticeService.addNotice(notice);
                    List<Map<String, Object>> userMaps = activityParticipateService.SelectActApplicantList(activity.getActivity_id());
                    // 将信息发送给活动中所有的用户
                    for (Map<String, Object> user : userMaps) {
                        UserNotice userNotice = new UserNotice((Long) user.get("user_id"), notice.getNoticeId(), 0);
                        userNoticeService.addUserNotice(userNotice);
                    }
                    // 更新活动提醒状态
                    activityService.modifyActivityRemind(activity.getActivity_id(), 1);
                }
            }
        }
    }

    // 每5秒钟执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    private void checkTaskTime() throws IOException {
        // 获取当前的时间
        var Now = new Date();
        // 获取所有事项
        List<Task> allTask = taskService.findAllTask();
        for (Task task : allTask) {
            var s_time = task.getStartTime();
            // 当前活动还没开始
            if (s_time.getTime() > Now.getTime()) {
                var isRemind = task.getIsRemind();

                //还有十分钟开始
                if (timeMinuteDiff(s_time, Now) <= 10 && (isRemind == 0 || isRemind == 1)) {
                    System.out.println("事项还有十分钟开始" + task.getTaskId());

                    String tagText = null;
                    //根据个性化标签tag生成不同的语音播报
                    if(task.getTag().equals("室内")){
                        tagText = "这是一个室内事项。";
                    }
                    else if (task.getTag().equals("户外")) {
                        tagText = "这是一个户外事项。" + GaodeUtil.getLiveWeatherText();
                    }
                    else
                        tagText = "";

                    ItemNotice itemNotice = new ItemNotice(task.getUserId(), task.getTaskTitle() + "事项开始提醒", "该事项还有十分钟就要开始了，请注意及时参加哦" + tagText, Now, task.getTaskId(), 0);
                    itemNoticeService.addItemNotice(itemNotice);
                    taskService.modifyTaskStatus(task.getTaskId(), 2);
                }

                //还有一小时开始
                else if (timeHourDiff(s_time, Now) <= 1 && isRemind == 0) {
                    System.out.println("事项还有一个小时开始" + task.getTaskId());

                    String tagText = null;
                    //根据个性化标签tag生成不同的语音播报
                    if(task.getTag() == "室内"){
                        tagText = "这是一个室内事项。";
                    }
                    else if (task.getTag() == "户外") {
                        tagText = "这是一个户外事项。" + GaodeUtil.getLiveWeatherText();
                    }

                    ItemNotice itemNotice = new ItemNotice(task.getUserId(), task.getTaskTitle() + "事项开始提醒", "该事项还有一小时就要开始了，请注意及时参加哦" + tagText, Now, task.getTaskId(), 0);
                    itemNoticeService.addItemNotice(itemNotice);
                    taskService.modifyTaskStatus(task.getTaskId(), 1);
                }
            }
        }
    }

    public FormatMessage formatMessage(Message m) {
        // 获取发送消息的用户的信息
        User user = userService.findUser(m.getSenderId());
        FormatUser formatUser = formatUser(user);
        // 格式化信息的状态和类型
        String status = formatStatus(m.getStatus());
        String type = formatType(m.getType());


        FormatMessage formatMessage = new FormatMessage(m.getMessageId(), status, type, m.getSendTime(), m.getContent(), formatUser, m.getActivityId().toString());
        return formatMessage;

    }

    private String formatType(int status) {
        if (status == 0) {
            return "text";
        } else if (status == 1) {
            return "image";
        } else if (status == 2) {
            return "file";
        }
        return null;
    }

    private String formatStatus(int status) {
        if (status == 0) {
            return "going";
        } else if (status == 1) {
            return "succeed";
        } else if (status == 2) {
            return "failed";
        }
        return null;
    }

    private FormatUser formatUser(User user) {
        return new FormatUser(user.getId(), user.getName(), user.getAvatar());
    }

}