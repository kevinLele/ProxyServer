package com.hq.CloudPlatform.BaseFrame.utils.validation;

import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationMethod;
import org.aopalliance.aop.Advice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 7/4/2017.
 */
public class ValidationPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    @Autowired
    @Lazy
    private HttpServletRequest request;

    public ValidationPointcutAdvisor(Advice advice) {
        setAdvice(advice);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return method.isAnnotationPresent(ValidationMethod.class);
    }
}
