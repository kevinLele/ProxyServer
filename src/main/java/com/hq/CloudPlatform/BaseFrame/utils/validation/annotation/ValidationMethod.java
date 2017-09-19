package com.hq.CloudPlatform.BaseFrame.utils.validation.annotation;

import java.lang.annotation.*;

/**
 * 用于标识需要对方法的参数实体进行校验的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ValidationMethod {

}
