package com.yl.test.service;

import com.yl.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "sso-auth", path = "/user")
public interface FeignService {

    @GetMapping(value = "/get/{id}")
    User get(@PathVariable("id") Integer id);

    @PostMapping(value = "/list")
    List<User> list(@RequestBody User user);

    @GetMapping(value = "/datasource/list")
    Set<String> list();

}
