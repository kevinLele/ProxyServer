package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by ZK on 8/25/2016.
 * 用于资源管理的URL重写拦截器
 */
public class MyRewriteInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MyRewriteInterceptor.class);

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {

        //实际地址
        URL realUrl = new URL("http://localhost/ProxyServer/restful/sys/testJson?aaa=1&bbb=2&aaa=%E4%B8%AD%E5%9B%BD1&c&d=3");
        exc.getDestinations().clear();

        String fromUrl = exc.getOriginalHostHeader() + exc.getOriginalRequestUri();
        log.debug("Form URL: " + fromUrl);

        String appendPath = exc.getStringProperty("appendPath");
        String query = exc.getStringProperty("query");
        StringBuffer destUrl = new StringBuffer();
        destUrl.append(realUrl.getProtocol()).append("://").append(realUrl.getHost());

        if (realUrl.getPort() > 0) {
            destUrl.append(":");
            destUrl.append(realUrl.getPort());
        }

        destUrl.append(realUrl.getPath());

        //最后一个字符如果是'/'则将其删除
        if (destUrl.charAt(destUrl.length() - 1) == '/') {
            destUrl.deleteCharAt(destUrl.length() - 1);
        }

        destUrl.append(appendPath);

        if (StringUtils.isNotBlank(realUrl.getQuery())) {
            destUrl.append("?").append(realUrl.getQuery());

            if (StringUtils.isNotBlank(query)) {
                destUrl.append("&").append(query);
            }
        } else {
            if (StringUtils.isNotBlank(query)) {
                destUrl.append("?").append(query);
            }
        }

        //用真实的地址去替换代理地址
        exc.getDestinations().add(destUrl.toString());
        log.debug("Original URL:" + realUrl);
        log.debug("Dest URL:" + destUrl.toString());

        return Outcome.CONTINUE;

    }
}
