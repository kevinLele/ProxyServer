package com.hq.CloudPlatform.ProxyServer.utils.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 7/4/2017.
 */
public class LogMethodInterceptor implements MethodInterceptor {

    @Autowired
    @Lazy
    private HttpServletRequest request;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }
}
