package com.caixc.mallchat.common.websocket;


import cn.hutool.json.JSONUtil;
import com.caixc.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.caixc.mallchat.common.websocket.domain.vo.res.WSBaseReq;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成");
        }else if (evt instanceof IdleStateEvent){
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE){
                System.out.println("用户下线");
                // todo 用户下线
                // 关闭连接
                ctx.channel().close();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case LOGIN:
                System.out.println("请求登录二维码");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("返回"));
            case AUTHORIZE:
                System.out.println("登录认证");
                break;
            case HEARTBEAT:
                System.out.println("心跳包");
                break;
            default:
                break;
        }
    }
}
