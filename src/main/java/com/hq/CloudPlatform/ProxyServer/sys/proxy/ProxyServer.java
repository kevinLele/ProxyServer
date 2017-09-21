package com.hq.CloudPlatform.ProxyServer.sys.proxy;

import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.RmAuthInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.RmCacheInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.RmRewriteInterceptor;
import com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor.RmURLAnalysisInterceptor;
import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;
import com.predic8.membrane.core.HttpRouter;
import com.predic8.membrane.core.interceptor.rewrite.RewriteInterceptor;
import com.predic8.membrane.core.rules.ServiceProxy;
import com.predic8.membrane.core.rules.ServiceProxyKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZK on 8/25/2016.
 * 代理服务器
 */
@Slf4j
public class ProxyServer {

    public static final String PROXY_URL = "PROXY_URL";

    public static final int PROXY_PORT = 0;

    public static final String PROXY_PATH = "/proxy/";

    public static void startProxy() throws Exception {
        log.info("Starting Proxy Server....begin.....");

        int proxyPort = Integer.parseInt(ConfigHelper.getValue("ProxyServer.port"));
        ServiceProxyKey key = new ServiceProxyKey("*", "*", PROXY_PATH + ".+", proxyPort);

        //设置为true表示启动路径过滤，否则只进行IP和端口的过滤
        key.setUsePathPattern(true);
        ServiceProxy sp = new ServiceProxy(key, PROXY_URL, PROXY_PORT);
        RmURLAnalysisInterceptor urlAnalysisInterceptor = new RmURLAnalysisInterceptor();
        RmAuthInterceptor authInterceptor = new RmAuthInterceptor();
        RmCacheInterceptor cacheInterceptor = new RmCacheInterceptor();
        RmRewriteInterceptor rewriteInterceptor = new RmRewriteInterceptor();

        List<RewriteInterceptor.Mapping> mappings = new ArrayList<>();
        mappings.add(new RewriteInterceptor.Mapping(PROXY_PATH + "(.*)", "/$1","rewrite"));
        rewriteInterceptor.setMappings(mappings);

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
