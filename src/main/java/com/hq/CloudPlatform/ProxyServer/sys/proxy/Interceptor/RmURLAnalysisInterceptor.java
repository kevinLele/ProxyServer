package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.hq.CloudPlatform.ProxyServer.sys.proxy.ProxyServer;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 9/13/2016.
 * 用于对用户请求进行解析，将serviceId以及authKey解析出来并存入Exchange中供之后执行的拦截器使用
 */
public class RmURLAnalysisInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RmURLAnalysisInterceptor.class);

    public RmURLAnalysisInterceptor() {
        super();
        setFlow(Flow.Set.REQUEST_RESPONSE);
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        /**
         * 设置不对响应结果进行压缩处理，否则通过代码请求时未进行解压缩操作
         * 而直接对内容进行解析时会出现乱码的问题
         */
        exc.getRequest().getHeader().removeFields("Accept-Encoding");

        ListIterator<String> it = exc.getDestinations().listIterator();
        StringBuffer sortedUrl = new StringBuffer();

        while (it.hasNext()) {
            String dest = it.next();
            URL proxyUrl = new URL("http", ProxyServer.PROXY_URL, ProxyServer.PROXY_PORT, ProxyServer.PROXY_PATH);

            //如果不是有效的代理地址则不进行任何处理并返回错误提示
            if (StringUtils.isNotBlank(dest) && dest.contains(proxyUrl.toString())) {
                URL requestUrl = new URL(dest);

                //获取路径中固定前缀/proxy/之后的路径
                String path = requestUrl.getPath().substring(ProxyServer.PROXY_PATH.length());
                sortedUrl.append(path);
                String[] pathItems = path.split("/");

                //请求参数
                String query = StringUtils.trimToEmpty(requestUrl.getQuery());
                MultiMap<String> queryParams = new MultiMap<>();
                UrlEncoded.decodeTo(query, queryParams, StandardCharsets.UTF_8);

                //路径中未包含type和key, /proxy/{type}/{key}
                if (null == pathItems || pathItems.length < 2
                        || StringUtils.isBlank(pathItems[0])
                        || StringUtils.isBlank(pathItems[1])) {
                    String msg = "不合法的请求地址！URL[" + dest + "]! 合法的请求地址必须是以“/proxy/{type}/{key}”开头的！";
                    exc.setResponse(getBadResponse(msg));

                    log.warn(msg);
                    return Outcome.ABORT;
                } else {
                    String type = pathItems[0];
                    String key = pathItems[1];
                    String authToken = null;

                    if (queryParams.containsKey("authToken")) {
                        authToken = queryParams.getString("authToken");

                        /*
                        获取authKey后，不需要再传该参数，这样在拼装url时就
                        没有authKey，提高缓存命中率
                        */
                        queryParams.remove("authToken");
                    }

                    StringBuffer appendPath = new StringBuffer();

                    if (pathItems.length > 2) {
                        for (int i = 2; i < pathItems.length; i++) {
                            if (StringUtils.isNotBlank(pathItems[i])) {
                                appendPath.append("/").append(pathItems[i]);
                            }
                        }
                    }

                    String queryString = encodeQueryString(queryParams);
                    sortedUrl.append("?").append(queryString);

                    exc.setProperty("type", type);
                    exc.setProperty("key", key);
                    exc.setProperty("authToken", authToken);
                    exc.setProperty("appendPath", appendPath.toString());
                    exc.setProperty("query", queryString);
                    exc.setProperty("sortedUrl", sortedUrl.toString());
                }
            } else {
                String msg = "不合法的请求地址！URL[" + dest + "]! 合法的请求地址必须是以“/proxy/{type}/{key}”开头的！";
                exc.setResponse(getBadResponse(msg));

                log.warn(msg);
                return Outcome.ABORT;
            }
        }

        return Outcome.CONTINUE;
    }

    @Override
    public Outcome handleResponse(Exchange exc) throws Exception {
        Response rsp = exc.getResponse();

        if (null != rsp) {
            rsp.getHeader().removeFields("Access-Control-Allow-Origin");
            rsp.getHeader().add("Access-Control-Allow-Origin", "*");
        }

        return super.handleResponse(exc);
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
                    values.stream().forEach(value -> queryStr.append(key).append("=").append(value).append("&"));
                }
            }
        });

        //将结尾多除的&符号去除
        if (queryStr.length() > 0 && queryStr.charAt(queryStr.length() - 1) == '&') {
            queryStr.deleteCharAt(queryStr.length() - 1);
        }

        return queryStr.toString();
    }

    private Response getBadResponse(String msg) {

        return Response.badRequest(msg).build();
    }
}