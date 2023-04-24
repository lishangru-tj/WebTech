package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.dto.FormatMessage;
import com.example.backend.dto.FormatMessageBoard;
import com.example.backend.dto.FormatUser;
import com.example.backend.entity.File;
import com.example.backend.entity.Message;
import com.example.backend.entity.MessageBoard;
import com.example.backend.entity.User;
import com.example.backend.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/3
 * @JDKVersion 17.0.4
 */
@Api(tags = {"Chat"})
@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageBoardService messageBoardService;

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Autowired
    WebSocket webSocket;

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

    private FormatUser formatUser(User user) {
        return new FormatUser(user.getId(), user.getName(), user.getAvatar());
    }

    public FormatMessage formatMessage(Message m) {
        // 获取发送消息的用户的信息
        User user = userService.findUser(m.getSenderId());
        FormatUser formatUser = formatUser(user);
        // 格式化信息的状态和类型
        String status = formatStatus(m.getStatus());
        String type = formatType(m.getType());
        if (Objects.equals(type, "file")) {
            // 是文件消息
            File file = fileService.getFileInfoById(m.getFileId());
            FormatMessage formatMessage = new FormatMessage(m.getMessageId(), status, type, m.getSendTime(), m.getContent(), file.getFileSize(), file.getFileName(), formatUser, m.getActivityId().toString());
            return formatMessage;
        } else {
            // 不是文件消息
            FormatMessage formatMessage = new FormatMessage(m.getMessageId(), status, type, m.getSendTime(), m.getContent(), formatUser, m.getActivityId().toString());
            return formatMessage;
        }
    }

    private FormatMessageBoard formatMessageBoard(MessageBoard messageBoard) {
        // 获取发送消息的用户的信息
        User user = userService.findUser(messageBoard.getUserId());
        return new FormatMessageBoard(messageBoard.getMessageBoardId(), messageBoard.getUserId(), user.getName(), messageBoard.getContent(), messageBoard.getCreateTime());
    }

    @ApiOperation("获取某个活动的所有消息")
    @GetMapping("getActivityAllMessage")
    public Result<List<FormatMessage>> findActivityAllMessage(@ApiParam(name = "activityId", value = "要查找的活动id", required = true)
                                                              @RequestParam("activityId") Long activityId) {
        try {
            ArrayList<FormatMessage> formatMessages = new ArrayList<>();
            List<Message> activityAllMessages = messageService.findActivityAllMessage(activityId);
            for (Message m : activityAllMessages) {
                FormatMessage formatMessage = formatMessage(m);
                formatMessages.add(formatMessage);
            }
            return Result.success(formatMessages);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    @ApiOperation("添加消息")
    @PostMapping("addMessage")
    public Result<String> addMessage(@ApiParam(name = "activityId", value = "活动ID", required = true)
                                     @RequestParam() Long activityId,
                                     @ApiParam(name = "senderId", value = "发送者ID", required = true)
                                     @RequestParam() Long senderId,
                                     @ApiParam(name = "status", value = "消息状态", required = true)
                                     @RequestParam() int status,
                                     @ApiParam(name = "type", value = "消息类型", required = true)
                                     @RequestParam() int type,
                                     @ApiParam(name = "sendTime", value = "发送时间", required = true)
                                     @RequestParam() Long sendTime,
                                     @ApiParam(name = "content", value = "消息内容")
                                     @RequestParam() String content) {
        try {
            Message message = new Message(activityId, senderId, status, type, sendTime, content);
            System.out.println(message);
            messageService.addMessage(message);

            FormatMessage formatMessage = formatMessage(message);
            webSocket.sendMessage(activityId.toString(), formatMessage);
            return Result.success("yes");
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    @ApiOperation("获取某个活动留言板中的所有消息")
    @GetMapping("getActivityAllMessageBoard")
    public Result<List<FormatMessageBoard>> findActivityAllMessageBoard(@ApiParam(name = "activityId", value = "要查找的活动id", required = true)
                                                                        @RequestParam("activityId") Long activityId) {
        try {
            ArrayList<FormatMessageBoard> formatMessageBoards = new ArrayList<>();
            List<MessageBoard> activityAllMessageBoard = messageBoardService.findActivityAllMessageBoard(activityId);
            for (MessageBoard mb : activityAllMessageBoard) {
                FormatMessageBoard formatMessageBoard = formatMessageBoard(mb);
                formatMessageBoards.add(formatMessageBoard);
            }
            return Result.success(formatMessageBoards);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    @ApiOperation("添加留言板消息")
    @PostMapping("addMessageBoard")
    public Result<String> addMessageBoard(@ApiParam(name = "activityId", value = "活动ID", required = true)
                                          @RequestParam() Long activityId,
                                          @ApiParam(name = "userId", value = "发送者ID", required = true)
                                          @RequestParam() Long userId,
                                          @ApiParam(name = "content", value = "内容", required = true)
                                          @RequestParam() String content,
                                          @ApiParam(name = "createTime", value = "发送时间", required = true)
                                          @RequestParam() String createTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(createTime);
            MessageBoard messageBoard = new MessageBoard(activityId, userId, content, date);
            messageBoardService.addMessageBoard(messageBoard);
            return Result.success("yes");
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }


    @ApiOperation("删除留言板消息")
    @DeleteMapping("deleteMessageBoard")
    public Result<Map<String, Object>> DeleteMessageBoard(
            @ApiParam(name = "message_board_id", value = "留言板信息ID", required = true)
            @RequestParam() Long message_board_id
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("message_board_id",message_board_id);
        try {
            Integer i=messageBoardService.deleteMessageBoard(message_board_id);
            map.put("i",i);

            return Result.success(map);
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }


}
