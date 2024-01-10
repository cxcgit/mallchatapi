package com.caixc.mallchat.common.websocket.service;

import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @author caixincheng
 * @Classname WsService
 * @Date 2024-01-06 17:11
 * @Created by cxc
 */
public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginReq(Channel channel) throws WxErrorException;

    void useOff(Channel channel);

    void scanLoginSuccess(Integer loginCode, Long id);

    void waitAuthorize(Integer loginCode);

    void authorizeSuccess(Integer loginCode);
}
