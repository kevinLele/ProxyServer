package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.predic8.membrane.core.interceptor.AbstractInterceptor;

/**
 * Created by ZK on 8/25/2016.
 * 用于对用户进行鉴权的拦截器
 */
public class RmAuthInterceptor extends AbstractInterceptor {

    /*private static final Logger log = LoggerFactory.getLogger(RmAuthInterceptor.class);

    private IUserAuthService userAuthService;

    public RmAuthInterceptor() {

        super();
        setFlow(Flow.Set.REQUEST);
        this.userAuthService = (IUserAuthService) ServiceBeanContext.getInstance().getBean("userAuthService");
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {

        String serviceId = exc.getStringProperty("serviceId");
        String authKey = exc.getStringProperty("authKey");
        Map<String, Object> checkResult = userAuthService.checkAuth(authKey, serviceId);

        if ("true".equals(checkResult.get("isAuthed"))) {
            //如果支持预览
            if ("true".equals(checkResult.get("isPreview"))) {
                exc.setProperty("isPreview","true");
            }

            return Outcome.CONTINUE;
        } else {
            String msg = "非法的请求，用户鉴权失败！[serviceId:" + serviceId + ", authKey:" + authKey + "]";
            exc.setResponse(Response.badRequest(msg).build());

            log.warn(msg);
            return Outcome.ABORT;
        }

    }*/
}
