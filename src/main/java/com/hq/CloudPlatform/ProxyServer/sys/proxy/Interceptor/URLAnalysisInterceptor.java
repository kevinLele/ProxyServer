package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Created by Administrator on 9/13/2016.
 * 用于对用户请求进行解析，将serviceId以及authKey解析出来并存入Exchange中供之后执行的拦截器使用
 */
@Slf4j
public class URLAnalysisInterceptor extends AbstractInterceptor {

    private static final String runningMode = ConfigHelper.getValue("running.mode");

    private static final String prefix = ConfigHelper.getValue("ProxyServer.prefix");

    public URLAnalysisInterceptor() {
        super();
        setFlow(Flow.Set.REQUEST_RESPONSE);
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        // 设置不对响应结果进行压缩处理，否则通过代码请求时未进行解压缩操作而直接对内容进行解析时会出现乱码的问题
        exc.getRequest().getHeader().removeFields("Accept-Encoding");

        URI originalRequestUri = new URI(exc.getOriginalRequestUri());
        String path = originalRequestUri.getPath().substring(1); //去掉第一个"/"字符
        String query = StringUtils.defaultString(originalRequestUri.getQuery());

        if (StringUtils.isNotBlank(path)) {
            String[] pathItems = path.split("/");

            MultiMap<String> queryParams = new MultiMap<>();
            UrlEncoded.decodeTo(query, queryParams, StandardCharsets.UTF_8);

            //路径中未包含/{prefix}/{type}/{key}
            if (null == pathItems || pathItems.length < 3
                    || !prefix.equals(pathItems[0])
                    || StringUtils.isBlank(pathItems[1])
                    || StringUtils.isBlank(pathItems[2])) {
                String msg = "URL[" + originalRequestUri + "]为不合法的请求地址！必须是以“/" + prefix + "/{type}/{key}”开头！";
                exc.setResponse(getBadResponse(msg));

                log.warn(msg);
                return Outcome.ABORT;
            } else {
                String type = pathItems[1];
                String key = pathItems[2];
                String authToken = null;

                if (queryParams.containsKey("authToken")) {
                    authToken = queryParams.getString("authToken");

                    // 获取authKey后，不需要再传该参数，这样在拼装url时就
                    // 没有authKey，提高缓存命中率
                    queryParams.remove("authToken");
                }

                StringBuffer appendPath = new StringBuffer();

                if (pathItems.length > 3) {
                    for (int i = 3; i < pathItems.length; i++) {
                        if (StringUtils.isNotBlank(pathItems[i])) {
                            appendPath.append("/").append(pathItems[i]);
                        }
                    }
                }

                exc.setProperty("type", type);
                exc.setProperty("key", key);
                exc.setProperty("authToken", authToken);
                exc.setProperty("appendPath", appendPath.toString());
                exc.setProperty("queryParams", queryParams);
            }
        } else {
            String msg = "URL[" + originalRequestUri + "]为不合法的请求地址！必须是以“/" + prefix + "/{type}/{key}”开头！";
            exc.setResponse(getBadResponse(msg));

            log.warn(msg);
            return Outcome.ABORT;
        }

        return Outcome.CONTINUE;
    }

    @Override
    public Outcome handleResponse(Exchange exc) throws Exception {
        Response rsp = exc.getResponse();

        if (null != rsp) {
            // 设置允许跨域访问的响应消息头
            rsp.getHeader().removeFields("Access-Control-Allow-Origin");
            rsp.getHeader().add("Access-Control-Allow-Origin", "*");
        }

        if(!"debug".equals(runningMode) && rsp.getStatusCode() >= 400 ){
            exc.setResponse(Response.internalServerError().build());
        }

        return super.handleResponse(exc);
    }

    private Response getBadResponse(String msg) {
        return Response.badRequest(msg).build();
    }
}
