package com.hq.CloudPlatform.BaseFrame.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 7/1/2017.
 */
public class SysReflectionUtils {
    /**
     * 通过方法名调用方法的工具方法
     *
     * @param object        要在其上执行方法的对象
     * @param methodName    方法名称
     * @param returnTypeCls 返回结果的类型
     * @param paramClasses  执行方法的参数的Class数组
     * @param args          参数对象
     * @param <T>           泛型返回值
     * @return
     */
    public static <T> T invokeMethodByName(Object object, String methodName,
                                            Class<T> returnTypeCls, Class[] paramClasses,
                                            Object... args) {
        Method method = ReflectionUtils.findMethod(
                object.getClass(),
                methodName,
                paramClasses
        );

        if (null == method) {
            return null;
        }

        return (T) ReflectionUtils.invokeMethod(method, object, args);
    }
}
