package com.hq.CloudPlatform.ProxyServer.sys;

import com.hq.CloudPlatform.ProxyServer.sys.proxy.ProxyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 7/3/2017.
 */
@Component
@Slf4j
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProxyServer proxyServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            proxyServer.startProxy();
        } catch (Exception e) {
            log.error("代理服务器启动失败...", e);
        }

        log.info("系统初始化完成...");
    }
}
