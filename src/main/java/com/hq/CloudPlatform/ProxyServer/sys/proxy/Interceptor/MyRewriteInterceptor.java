package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.hq.CloudPlatform.ProxyServer.entity.ProxyInfo;
import com.hq.CloudPlatform.ProxyServer.service.IProxyInfoService;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZK on 8/25/2016.
 * 用于资源管理的URL重写拦截器
 */
@Component
public class MyRewriteInterceptor extends AbstractInterceptor {

    @Autowired
    @Qualifier("proxyInfoService")
    private IProxyInfoService proxyInfoService;

    private static final Logger log = LoggerFactory.getLogger(MyRewriteInterceptor.class);

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        // 将默认生成的目标地址清除
        exc.getDestinations().clear();
        String key = exc.getStringProperty("key");

        ProxyInfo proxyInfo = proxyInfoService.findByKey(key);

        if(null == proxyInfo){
            String msg = "请求的地址[" + exc.getOriginalRequestUri() + "]不存在！";
            exc.setResponse(Response.badRequest(msg).build());

            log.warn(msg);
            return Outcome.ABORT;
        }

        // 要访问的实际地址
        URL realUrl = new URL(proxyInfo.getOriginalUrl());
        String realPath = realUrl.getPath();
        String realQuery = realUrl.getQuery();

        // 访问的代理地址
        String proxyUrl = exc.getOriginalHostHeader() + exc.getOriginalRequestUri();
        log.debug("Proxy URL: " + proxyUrl);

        String appendPath = exc.getStringProperty("appendPath");
        MultiMap<String> queryParams = (MultiMap<String>) exc.getProperty("queryParams");

        StringBuffer destUrl = new StringBuffer();
        destUrl.append(realUrl.getProtocol()).append("://").append(realUrl.getHost());

        if (realUrl.getPort() > 0) {
            destUrl.append(":");
            destUrl.append(realUrl.getPort());
        }

        destUrl.append(realPath);

        //最后一个字符如果是'/'则将其删除
        if (destUrl.charAt(destUrl.length() - 1) == '/') {
            destUrl.deleteCharAt(destUrl.length() - 1);
        }

        destUrl.append(appendPath);

        if (StringUtils.isNotBlank(realQuery)) {
            MultiMap<String> realQueryParams = new MultiMap<>();
            UrlEncoded.decodeTo(realQuery, realQueryParams, StandardCharsets.UTF_8);

            realQueryParams.keySet().forEach(pkey -> {
                if (queryParams.containsKey(pkey)) {
                    queryParams.getValues(pkey).addAll(realQueryParams.getValues(pkey));
                } else {
                    queryParams.addValues(pkey, realQueryParams.getValues(pkey));
                }
            });
        }

        if (queryParams.size() > 0) {
            destUrl.append("?").append(encodeQueryString(queryParams));
        }

        //用真实的地址去替换代理地址
        exc.getDestinations().add(destUrl.toString());
        log.debug("Original URL:" + realUrl);
        log.debug("Dest URL:" + destUrl.toString());

        return Outcome.CONTINUE;
    }

    /**
     * 对请求参数进行排序，从而提高缓存的命中率
     *
     * @param queryParams
     * @return
     */
    private String encodeQueryString(MultiMap<String> queryParams) {
        StringBuffer queryStr = new StringBuffer();
        List<String> keyList = new ArrayList<>(queryParams.keySet());
        Collections.sort(keyList);

        keyList.stream().forEach(key -> {
            List<String> values = queryParams.getValues(key);

            if (values != null) {
                Collections.sort(values);

                if (values.size() > 0) {
                    values.stream().forEach(value -> {
                        try {
                            queryStr.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
                        } catch (UnsupportedEncodingException e) {
                            log.error("URL encoding error!", e);
                        }
                    });
                }
            }
        });

        //将结尾多除的&符号去除
        if (queryStr.length() > 0 && queryStr.charAt(queryStr.length() - 1) == '&') {
            queryStr.deleteCharAt(queryStr.length() - 1);
        }

        return queryStr.toString();
    }
}
