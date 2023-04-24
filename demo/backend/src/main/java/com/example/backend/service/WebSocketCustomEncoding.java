package com.example.backend.service;

import com.alibaba.fastjson.JSON;
import com.example.backend.dto.FormatMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketCustomEncoding implements Encoder.Text<FormatMessage> {


    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(FormatMessage formatMessage) throws EncodeException {
        assert formatMessage!=null;
        return JSON.toJSONString(formatMessage);
    }
}
