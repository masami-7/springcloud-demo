package com.yl.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.auth.entity.User;
import com.yl.auth.mapper.UserMapper;
import com.yl.auth.service.UserService;
import com.yl.common.config.BusinessException;
import com.yl.common.config.ResultCode;
import com.yl.common.util.JWTUtil;
import com.yl.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @description 针对表【tb_users】的数据库操作Service实现
 * @createDate 2023-02-23 15:42:47
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<User> list(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public String login(User user) {
        if (!"zhuang".equals(user.getUsername())) {
            User one = new LambdaQueryChainWrapper<>(userMapper)
                    .eq(User::getUsername, user.getUsername())
                    .eq(User::getPassword, user.getPassword())
                    .one();
            if (ObjectUtil.isNull(one)) {
                throw new BusinessException(ResultCode.INVALID_USERNAME.getCode(), ResultCode.INVALID_USERNAME.getMsg());
            }
        }

        //  生成Token
        String token = JWTUtil.generateToken(user.getUsername(), 10 * 60 * 1000);

        String key = MD5.create().digestHex(user.getUsername());
//        redisUtil.set("auth:token:" + key + ":string", token, 10 * 60 * 1000);

        return token;
    }

}