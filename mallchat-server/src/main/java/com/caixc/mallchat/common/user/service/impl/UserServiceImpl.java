package com.caixc.mallchat.common.user.service.impl;

import com.caixc.mallchat.common.user.domain.entity.User;
import com.caixc.mallchat.common.user.mapper.UserMapper;
import com.caixc.mallchat.common.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
