package com.caixc.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.caixc.mallchat.common.common.constant.RedisKey;
import com.caixc.mallchat.common.common.uitls.JwtUtils;
import com.caixc.mallchat.common.common.uitls.RedisUtils;
import com.caixc.mallchat.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName LoginServiceImpl.java
 * @Description TODO
 * @createTime 2024年01月10日 13:50:00
 */
@Service
public class LoginServiceImpl implements LoginService {
    //token过期时间
    private static final Integer TOKEN_EXPIRE_DAYS = 5;
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public String login(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
//        String token = RedisUtils.getStr(key);
//        if (StrUtil.isNotBlank(token)) {
//            return token;
//        }
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(key, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);//token过期用redis中心化控制，初期采用5天过期，剩1天自动续期的方案。后续可以用双token实现
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}
