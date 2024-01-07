package com.caixc.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.caixc.mallchat.common.user.domain.entity.User;
import com.caixc.mallchat.common.user.service.UserService;
import com.caixc.mallchat.common.user.service.WxMsgService;
import com.caixc.mallchat.common.user.service.adapter.TextBuilder;
import com.caixc.mallchat.common.user.service.adapter.UserAdapter;
import com.caixc.mallchat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caixincheng
 * @Classname WxMsgServiceImpl
 * @Date 2024-01-07 13:28
 * @Created by cxc
 */
@Service
@Slf4j
public class WxMsgServiceImpl implements WxMsgService {
    /**
     * 用户的openId和前端登录场景code的映射关系
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAN = new ConcurrentHashMap<>();
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Value("${wx.mp.callback}")
    private String callback;
    @Resource
    private UserService userService;
    @Resource
    private WebSocketService webSocketService;

    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        // 微信用户唯一ID
        String openid = wxMpXmlMessage.getFromUser();
        // 事件码
        Integer loginCode = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(loginCode)){
            return null;
        }
        User user = userService.getById(openid);
        // 用户存在  并且已经授权 调用登录逻辑
        if (Objects.nonNull(user) && StrUtil.isNotBlank(user.getAvatar())){
            // todo 登录返回token   webscoket返回登录成功
            webSocketService.scanLoginSuccess(loginCode,user.getId());
            return null;
        }
        if (Objects.isNull(user)){
            // 注册
            userService.register(UserAdapter.buileUserSave(openid));
        }
         // 用户授权
        WAIT_AUTHORIZE_MAN.put(openid,loginCode);
        webSocketService.waitAuthorize(loginCode);
        String skipUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback+"/wx/portal/public/callBack"));
        return new TextBuilder().build("请点击链接授权：<a href=\"" + skipUrl + "\">登录</a>",wxMpXmlMessage,wxMpService);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        userService.authorize(userInfo);
        Integer loginCode = WAIT_AUTHORIZE_MAN.remove(userInfo.getOpenid());
        // 通过code找到websoket通道
    }

    /**
     * 统一处理事件码
     * @param wxMpXmlMessage
     * @return
     */
    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            //扫码关注的渠道事件有前缀，需要去除
            String eventKey = wxMpXmlMessage.getEventKey();
            eventKey.replace("qrscene_", "");
            return Integer.parseInt(eventKey);
        } catch (Exception e) {
            log.error("getEventKey error eventKey:{}",wxMpXmlMessage.getEventKey(),e);
            return null;
        }
    }
}
