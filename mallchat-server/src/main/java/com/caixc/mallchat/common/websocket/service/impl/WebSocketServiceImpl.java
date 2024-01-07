package com.caixc.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.caixc.mallchat.common.user.domain.entity.User;
import com.caixc.mallchat.common.user.service.UserService;
import com.caixc.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.caixc.mallchat.common.websocket.service.WebSocketService;
import com.caixc.mallchat.common.websocket.service.adapter.WSAbapter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author caixincheng
 * @Classname WebSocketServiceImpl
 * @Date 2024-01-06 17:13
 * @Created by cxc
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static final Duration EXPIRE_TIME = Duration.ofHours(1);
    private static final Long MAX_MUM_SIZE = 10000L;
    /**
     * 所有请求登录的code与channel关系
     */
    public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_TIME)
            .maximumSize(MAX_MUM_SIZE)
            .build();
    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap() {
        return ONLINE_WS_MAP;
    }

    @Resource
    @Lazy
    private WxMpService wxMpService;
    @Resource
    private UserService userService;


    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO());
    }

    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        // 生成随机数  并且保存到本地缓存map中
        Integer code = generateLoginCode(channel);
        // 请求微信获取二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code,(int)EXPIRE_TIME.getSeconds());
        // 通过websoket返回给前端
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(WSAbapter.buildLoginResp(wxMpQrCodeTicket))));
    }


    @Override
    public void useOff(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void scanLoginSuccess(Integer loginCode, Long id) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if (Objects.isNull(channel)){
            return;
        }
        User user = userService.getById(id);
        WAIT_LOGIN_MAP.invalidate(loginCode);
        // 登录获取token
        String token = "";
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(WSAbapter.buildLoginSuccessResp(token,user))));
    }

    @Override
    public void waitAuthorize(Integer loginCode) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        WSAbapter.buildWaitAuthorizeResp();
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(WSAbapter.buildWaitAuthorizeResp())));
    }

    /**
     * 获取不重复的登录的code，微信要求最大不超过int的存储极限
     * 防止并发，可以给方法加上synchronize，也可以使用cas乐观锁
     *
     * @return
     */
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (WAIT_LOGIN_MAP.asMap().containsKey(code));
        WAIT_LOGIN_MAP.put(code,channel);
        return code;
    }
}
