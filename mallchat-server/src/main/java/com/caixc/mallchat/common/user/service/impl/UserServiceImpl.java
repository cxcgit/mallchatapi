package com.caixc.mallchat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixc.mallchat.common.user.domain.entity.User;
import com.caixc.mallchat.common.user.mapper.UserMapper;
import com.caixc.mallchat.common.user.service.UserService;
import com.caixc.mallchat.common.user.service.adapter.UserAdapter;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2024-01-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }

    @Override
    public void register(User user) {
        this.save(user);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        User user = getUserByOpenId(userInfo.getOpenid());
        User update = UserAdapter.buileAuthorizeUser(user.getId(), userInfo);
        this.updateById(update);
    }
}
