package com.hq.CloudPlatform.ProxyServer.sys.proxy;

import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.AuthInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.CacheInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.MyRewriteInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.URLAnalysisInterceptor;
import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;
import com.predic8.membrane.core.HttpRouter;
import com.predic8.membrane.core.rules.ServiceProxy;
import com.predic8.membrane.core.rules.ServiceProxyKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ZK on 8/25/2016.
 * 代理服务器
 */
@Component
@Slf4j
public class ProxyServer {

    public static final String PROXY_URL = "PROXY_URL";

    public static final int PROXY_PORT = 0;

    @Autowired
    private URLAnalysisInterceptor urlAnalysisInterceptor;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private CacheInterceptor cacheInterceptor;

    @Autowired
    private MyRewriteInterceptor rewriteInterceptor;

    public void startProxy() throws Exception {
        log.info("Starting Proxy Server....begin.....");

        int proxyPort = Integer.parseInt(ConfigHelper.getValue("ProxyServer.port"));
        ServiceProxyKey key = new ServiceProxyKey("*", "*", "*", proxyPort);

        // 设置为true表示启动路径过滤，否则只进行IP和端口的过滤
        // 此处设置为false的目的是过滤的工作都交给URLAnalysisInterceptor来统一处理，不使用系统默认的处理方式
        key.setUsePathPattern(false);
        ServiceProxy sp = new ServiceProxy(key, PROXY_URL, PROXY_PORT);

        sp.getInterceptors().add(urlAnalysisInterceptor);
        //sp.getInterceptors().add(authInterceptor);
        //sp.getInterceptors().add(cacheInterceptor);
        sp.getInterceptors().add(rewriteInterceptor);
        HttpRouter router = new HttpRouter();

        try {
            router.add(sp);
            router.init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Starting Proxy Server [FAILED]....end.....", e);
            throw e;
        }

        log.info("Starting Proxy Server [SUCCESSFUL]....end.....");
    }
}
