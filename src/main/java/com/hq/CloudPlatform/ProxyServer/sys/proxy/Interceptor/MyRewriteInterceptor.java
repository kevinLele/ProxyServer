package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.interceptor.Outcome;
import com.predic8.membrane.core.interceptor.rewrite.RewriteInterceptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ListIterator;

/**
 * Created by ZK on 8/25/2016.
 * 用于资源管理的URL重写拦截器
 */
public class MyRewriteInterceptor extends com.predic8.membrane.core.interceptor.rewrite.RewriteInterceptor {

    /*private static final Logger log = LoggerFactory.getLogger(MyRewriteInterceptor.class);

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {

        ListIterator<String> it = exc.getDestinations().listIterator();

        while (it.hasNext()) {
            String fromUrl = it.next();
            log.debug("Form URL: " + fromUrl);

            String appendPath = exc.getStringProperty("appendPath");
            String query = exc.getStringProperty("query");

            //获取serviceId获取相应的图层配置信息
            Resource resource = (Resource)exc.getProperty("resource");

            if (resource != null && StringUtils.isNotBlank(resource.getUrl())) {
                //获取图层原始的地址
                URL originalUrl = new URL(resource.getUrl());

                StringBuffer destUrl = new StringBuffer();
                destUrl.append(originalUrl.getProtocol()).append("://").append(originalUrl.getHost());

                if (originalUrl.getPort() > 0) {
                    destUrl.append(":");
                    destUrl.append(originalUrl.getPort());
                }

                destUrl.append(originalUrl.getPath());

                if (destUrl.charAt(destUrl.length() - 1) == '/') {
                    destUrl.deleteCharAt(destUrl.length() - 1);
                }

                destUrl.append(appendPath);

                if (StringUtils.isNotBlank(originalUrl.getQuery())) {
                    destUrl.append("?").append(originalUrl.getQuery());

                    if (StringUtils.isNotBlank(query)) {
                        destUrl.append("&").append(query);
                    }
                } else {
                    if (StringUtils.isNotBlank(query)) {
                        destUrl.append("?").append(query);
                    }
                }

                //用真实的图层地址去替换代理地址
                it.set(destUrl.toString());
                log.debug("Original URL:" + originalUrl);
                log.debug("Dest URL:" + destUrl.toString());

                return Outcome.CONTINUE;
            } else {
                String msg = "未找到相应的图层信息！";
                exc.setResponse(Response.badRequest(msg).build());

                return Outcome.ABORT;
            }
        }

        return Outcome.CONTINUE;
    }*/
}
