package com.yl.gateway.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

@Component
public class NacosInstancesChangeEventListener extends Subscriber<InstancesChangeEvent> {

    @Resource
    private CacheManager defaultLoadBalancerCacheManager;

    @PostConstruct
    public void registerToNotifyCenter() {
        NotifyCenter.registerSubscriber(this);
//        ThreadUtil.execute(new Runnable() {
//            @Override
//            public void run() {
//                Cache cache = defaultLoadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
//                while (true) {
//                    if (cache != null) {
//                        List<ServiceInstance> list = (List) cache.get("sso-test", List.class);
//                        System.out.println(JSONUtil.toJsonStr(list));
//                        ThreadUtil.sleep(1 * 1000);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onEvent(InstancesChangeEvent instancesChangeEvent) {
        System.out.println(instancesChangeEvent.getServiceName());
        instancesChangeEvent.getHosts().forEach(e -> {
            System.out.println(JSONUtil.toJsonStr(e));
        });
        Cache cache = defaultLoadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
        if (cache != null) {
            cache.evict(instancesChangeEvent.getServiceName());
        }
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }

}
