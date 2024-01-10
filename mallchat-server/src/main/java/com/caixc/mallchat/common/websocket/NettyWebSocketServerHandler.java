package com.caixc.mallchat.common.websocket;


import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.caixc.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.caixc.mallchat.common.websocket.domain.vo.res.WSBaseReq;
import com.caixc.mallchat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
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

    private WebSocketService webSocketService;

    /**
     * 创建websoket连接的时候
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        webSocketService =  SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成");
        }else if (evt instanceof IdleStateEvent){
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE){
                System.out.println("用户下线");
                // todo 用户下线
                useOffLine(ctx.channel());
            }
        }
    }

    /**
     * 断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        useOffLine(ctx.channel());
    }

    /**
     * 用户下线
     * @param channel
     */
    private void useOffLine(Channel channel){
        webSocketService.useOff(channel);
        channel.close();
    }

    /**
     * 读取客户端发送的请求报文
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case LOGIN:
                this.webSocketService.handleLoginReq(ctx.channel());
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
