package com.yl.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.auth.entity.User;

import java.util.List;

/**
 * @author admin
 * @description 针对表【tb_users】的数据库操作Service
 * @createDate 2023-02-23 15:42:47
 */
public interface UserService extends IService<User> {

    List<User> list(User user);

    User get(Integer id);

    String login(User user);

}