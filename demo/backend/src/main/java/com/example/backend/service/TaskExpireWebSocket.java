package com.example.backend.service;

import com.example.backend.dto.FormatMessage;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 2051196 黄彦铭
 * @Date 2022/12/10
 * @JDKVersion 17.0.4
 */
@Component
@ServerEndpoint(value = "/taskExpireWebSocket", encoders = WebSocketCustomEncoding.class)
public class TaskExpireWebSocket {
    private Session session;
    private String taskId;

    private static final Map<String, ArrayList<TaskExpireWebSocket>> webSocketMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("创建会话！session"+session);
        Map<String, List<String>> map = session.getRequestParameterMap();
        taskId = map.get("taskId").get(0);
        // 判断该事项是否已经在map中存在
        if (webSocketMap.containsKey(taskId)) {
            webSocketMap.get(taskId).add(this);
        } else {
            ArrayList<TaskExpireWebSocket> webSockets = new ArrayList<>();
            webSockets.add(this);
            webSocketMap.put(taskId, webSockets);
        }

        System.out.println("有新的连接，组号为：" + taskId + "，当前组中总数为：" + webSocketMap.get(taskId).size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.get(taskId).remove(this);
        System.out.println("断开连接，组号为：" + taskId + "，当前组中总数为：" + webSocketMap.get(taskId).size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("收到客户端发来的消息");
    }

    public boolean sendMessage(String taskId, FormatMessage formatMessage) {
        System.out.println(webSocketMap);
        if(webSocketMap == null || webSocketMap.size() == 0)
            return false;
        // 只向同组的成员广播消息
        ArrayList<TaskExpireWebSocket> webSockets = webSocketMap.get(taskId);
        System.out.println("webSocketMap="+webSocketMap);
        for (TaskExpireWebSocket webSocket : webSockets) {
            // 不向自身广播
            if (webSocket != this) {
                System.out.println("广播消息" + formatMessage);
                try {
                    webSocket.session.getBasicRemote().sendObject(formatMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }


}
