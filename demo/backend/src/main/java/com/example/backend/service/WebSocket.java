package com.example.backend.service;

import com.example.backend.dto.FormatMessage;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 2051196 刘一飞
 * @Date 2022/12/4
 * @JDKVersion 17.0.4
 */
@Component
@ServerEndpoint(value = "/webSocket", encoders = WebSocketCustomEncoding.class)
public class WebSocket {
    private Session session;
    private String group;
    private static final Map<String, ArrayList<WebSocket>> webSocketMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Map<String, List<String>> map = session.getRequestParameterMap();
        group = map.get("activityId").get(0);
        // 判断该群聊是否已经在map中存在
        if (webSocketMap.containsKey(group)) {
            webSocketMap.get(group).add(this);
        } else {
            ArrayList<WebSocket> webSockets = new ArrayList<>();
            webSockets.add(this);
            webSocketMap.put(group, webSockets);
        }

        System.out.println("有新的连接，组号为：" + group + "，当前组中总数为：" + webSocketMap.get(group).size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.get(group).remove(this);
        System.out.println("断开连接，组号为：" + group + "，当前组中总数为：" + webSocketMap.get(group).size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("收到客户端发来的消息");
    }

    public void sendMessage(String activityId, FormatMessage formatMessage) {
        // 只向同组的成员广播消息
        ArrayList<WebSocket> webSockets = webSocketMap.get(activityId);
        for (WebSocket webSocket : webSockets) {
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
    }


}
