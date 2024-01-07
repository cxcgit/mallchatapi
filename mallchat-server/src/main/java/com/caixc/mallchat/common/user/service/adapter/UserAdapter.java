package com.caixc.mallchat.common.user.service.adapter;

import com.caixc.mallchat.common.user.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @author caixincheng
 * @Classname UserAdapter
 * @Date 2024-01-07 14:08
 * @Created by cxc
 */
public class UserAdapter {

    public static User buileUserSave(String openId){
        return User.builder().openId(openId).build();
    }

    public static User buileAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        return User.builder()
                .id(id)
                .openId(userInfo.getOpenid())
                .name(userInfo.getNickname())
                .avatar(userInfo.getHeadImgUrl())
                .sex(userInfo.getSex())
                .build();
    }
}
