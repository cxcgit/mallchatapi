package com.caixc.mallchat.common.user.service;

import com.caixc.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2024-01-02
 */
public interface UserService extends IService<User> {

    User getUserByOpenId(String openId);

    /**
     * 用户注册
     * @param user
     */
    void register(User user);

    /**
     * 用户授权  补全用户信息
     * @param userInfo
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
