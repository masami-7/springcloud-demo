package com.yl.gateway.config;

import cn.hutool.http.HttpUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NacosTemplate {

    //主要用作服务方面的管理功能
    private NamingService namingService;

    //初始化namingService;
    public NacosTemplate() {
        try {
            namingService = NacosFactory.createNamingService("http://47.96.43.200:8848");
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册服务
     *
     * @param instance
     * @throws Exception
     */
    public void registerNacosServer(Instance instance) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", instance.getServiceName());
        map.put("ip", instance.getIp());
        map.put("port", instance.getPort());
        // 因为api手动注册的客户端没有sdk，需要自己实现心跳机制
        System.out.println(HttpUtil.post("http://47.96.43.200:8848/nacos/v1/ns/instance", map));

        // 因为是在当前sdk注册，所以心跳是这边发送的，需要一个代理服务？但是没有文档参考
//        namingService.registerInstance(instance.getServiceName(), instance);
//        namingService.subscribe(instance.getServiceName(), event ->
//                System.out.println(" event " + JSONUtil.toJsonStr(event)));
    }

    /**
     * 删除服务
     *
     * @param instance
     * @throws Exception
     */
    public void deleteNacosServer(Instance instance) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", instance.getServiceName());
        map.put("ip", instance.getIp());
        map.put("port", instance.getPort());
//        System.out.println(HttpUtil.createRequest(Method.DELETE,"http://47.96.43.200:8848/nacos/v1/ns/instance")
//                .form(map).execute().body());
        namingService.deregisterInstance(instance.getServiceName(), instance.getIp(), instance.getPort());
//        namingService.unsubscribe(instance.getServiceName(), event -> {});
    }

    /**
     * 发现服务
     * 判断各服务状态
     *
     * @param serviceName
     * @throws Exception
     */
    public List<Instance> discoverNacosServer(String serviceName) throws Exception {
        return namingService.getAllInstances(serviceName);
    }

}
