package com.caixc.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.caixc.mallchat.common.common.constant.RedisKey;
import com.caixc.mallchat.common.common.uitls.JwtUtils;
import com.caixc.mallchat.common.common.uitls.RedisUtils;
import com.caixc.mallchat.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
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
    public static final Integer TOKEN_RENEWAL_DAYS = 1;
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean verify(String token) {
        return false;
    }

    /***
     * @Description: token续期
     * @Author: caixc
     * @Date: 2024/1/17 17:04
     * @param token:
     * @return: void
     */
    @Async
    @Override
    public void renewalTokenIfNecessary(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return;
        }
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
        long expireDays = RedisUtils.getExpire(key, TimeUnit.DAYS);
        if (expireDays == -2) {//不存在的key
            return;
        }
        if (expireDays < TOKEN_RENEWAL_DAYS) {//小于一天的token帮忙续期
            RedisUtils.expire(key, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
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
