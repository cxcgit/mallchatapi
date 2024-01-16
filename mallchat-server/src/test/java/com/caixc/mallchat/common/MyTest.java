package com.caixc.mallchat.common;

import com.caixc.mallchat.common.common.constant.RedisKey;
import com.caixc.mallchat.common.common.uitls.JwtUtils;
import com.caixc.mallchat.common.common.uitls.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName Test.java
 * @Description TODO
 * @createTime 2024年01月16日 15:09:00
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MyTest {

    //token过期时间
    private static final Integer TOKEN_EXPIRE_DAYS = 5;
    @Resource
    private JwtUtils jwtUtils;
    @Test
    public void test(){
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, 1L);
//        String token = RedisUtils.getStr(key);
//        if (StrUtil.isNotBlank(token)) {
//            return token;
//        }
        String token = jwtUtils.createToken(1L);
        RedisUtils.set(key, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);//token过期用redis中心化控制，初期采用5天过期，剩1天自动续期的方案。后续可以用双token实现
    }

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redis() {
//        stringRedisTemplate.opsForValue().set("name","卷心菜");
//        String name = (String) redisTemplate.opsForValue().get("name");
//        System.out.println(name); //卷心菜

        RLock lock = redissonClient.getLock("name");
        boolean b = lock.tryLock();
        int holdCount = lock.getHoldCount();
//        lock.isLocked();
        System.out.println(holdCount);

    }
}
