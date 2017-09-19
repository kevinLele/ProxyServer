package com.hq.CloudPlatform.ProxyServer.utils.log.annotation;

import java.lang.annotation.*;

/**
 * 用于描述是哪个模块的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Module {
    
    String value() default "";
}
