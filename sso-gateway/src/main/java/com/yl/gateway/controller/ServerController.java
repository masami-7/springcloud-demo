package com.yl.gateway.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.yl.gateway.config.NacosTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/server")
public class ServerController {

    @Autowired
    NacosTemplate nacosTemplate;

    @PostMapping("/add")
    public void add(@RequestBody Instance instance) throws Exception {
        nacosTemplate.registerNacosServer(instance);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody Instance instance) throws Exception {
        nacosTemplate.deleteNacosServer(instance);
    }

    @GetMapping("/get/{name}")
    public List<Instance> get(@PathVariable String name) throws Exception {
        return nacosTemplate.discoverNacosServer(name);
    }

}
