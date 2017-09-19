package com.hq.CloudPlatform.BaseFrame.utils.validation.annotation;

import java.lang.annotation.*;

/**
 * 用于标识需要进行校验的实体类的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ValidationBean {

}
