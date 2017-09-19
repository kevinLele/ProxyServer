package com.hq.CloudPlatform.ProxyServer.utils.dictionary;

import com.hq.CloudPlatform.ProxyServer.service.IBaseService;
import com.hq.CloudPlatform.ProxyServer.utils.dictionary.annotation.HasDictionaryFields;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 7/25/2017.
 */
@Slf4j
public class DictionaryPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    private Pattern methodPatten;

    public DictionaryPointcutAdvisor(Advice advice, String methodExpression) {
        log.info("初始始数据字典Advisor:");
        setAdvice(advice);

        // 默认只对以find开头的方法进行拦截
        if (StringUtils.isBlank(methodExpression)) {
            methodExpression = "^find.*";
        }

        methodPatten = Pattern.compile(methodExpression);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Class returnType = method.getReturnType();

        if (null != returnType && returnType.isAnnotationPresent(HasDictionaryFields.class)) {
            return true;
        } else if (IBaseService.class.isAssignableFrom(targetClass)) { //只对实现了IBaseService的类进行处理
            // 根据配置的方法名进行过滤
            Matcher matcher = methodPatten.matcher(method.getName());

            if (matcher.matches()) {
                log.info(method.getName() + ":" + targetClass.getName());
                return matcher.matches();
            }
        }

        return false;
    }
}
