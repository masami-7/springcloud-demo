package com.yl.test.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.yl.common.entity.User;
import com.yl.test.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    FeignService feignService;

    /**
     * feign调用
     */
    @GetMapping("/get/{id}")
    User get(@PathVariable("id") Integer id) {
        return feignService.get(id);
    }


    /**
     * feign调用
     */
    @PostMapping("/list")
    List<User> list(User user) {
        return feignService.list(user);
    }

    /**
     * feign调用
     */
    @GetMapping("/datasource/list")
    Set<String> list() {
        return feignService.list();
    }

    @GetMapping("/temp")
    Map temp() {
        System.out.println("1111111111111111111");
        return new HashMap();
    }

    @GetMapping("/temp2/{num}")
    void temp2(@PathVariable int num) {
        if (num == 1) {
            System.out.println("正常");
        } else {
            System.out.println("1111111111111111111");
            throw new RuntimeException();
        }
    }

    @GetMapping("/temp3")
    void temp3() {
        System.out.println("2222222222222222222");
        ThreadUtil.sleep(5 * 1000);
    }

}
