package com.yl.auth.controller;

import com.yl.auth.entity.User;
import com.yl.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/list")
    public List<User> list(@RequestBody User user) {
        return userService.list(user);
    }

    @GetMapping("/get/{id}")
    public User get(@PathVariable("id") Integer id) {
        return userService.get(id);
    }

    @GetMapping("/test01")
    public String get01() {
        return "测试字符串返回";
    }

    @GetMapping("/test02")
    public String get02() {
        throw new NullPointerException();
    }

    @GetMapping("/test03")
    public void get03() throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = contextClassLoader.getResources("META-INF/spring.factories");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.getPath());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(User user) {
        return userService.login(user);
    }

    @GetMapping("/loginPage")
    public String loginPage(@RequestParam(name = "login_callback", required = false) String callback) {
        return "login";
    }

}
