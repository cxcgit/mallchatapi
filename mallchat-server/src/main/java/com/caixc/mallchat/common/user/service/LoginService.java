package com.caixc.mallchat.common.user.service;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName LoginService.java
 * @Description TODO
 * @createTime 2024年01月10日 13:49:00
 */
public interface LoginService {


    /**
     * 校验token是不是有效
     *
     * @param token
     * @return
     */
    boolean verify(String token);

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);
}
