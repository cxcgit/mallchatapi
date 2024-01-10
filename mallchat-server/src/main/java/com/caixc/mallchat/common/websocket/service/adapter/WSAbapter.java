package com.caixc.mallchat.common.websocket.service.adapter;

import com.caixc.mallchat.common.user.domain.entity.User;
import com.caixc.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.caixc.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.caixc.mallchat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.caixc.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * @author caixincheng
 * @Classname WSAbapter
 * @Date 2024-01-06 17:52
 * @Created by cxc
 */

public class WSAbapter {

    public static WSBaseResp<WSLoginUrl> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(WSLoginUrl.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
        return wsBaseResp;
    }

    public static WSBaseResp<?> buildScanSuccessResp() {
        WSBaseResp<?> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }

    public static WSBaseResp<WSLoginSuccess> buildLoginSuccessResp(String token , User user) {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        wsBaseResp.setData(WSLoginSuccess.builder()
                .uid(user.getId())
                .name(user.getName())
                .token(token)
                .avatar(user.getAvatar())
                .power(1)
                .build());
        return wsBaseResp;
    }


}
