package com.hq.CloudPlatform.BaseFrame.utils.log.annotation;

import java.lang.annotation.*;

/**
 * 用于给方法标识需要记录操作日志的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperateLog {

    /**
     * 功能模块
     *
     * @return
     */
    String module() default "";

    /**
     * 执行的动作
     *
     * @return
     */
    String action() default "";

    /**
     * 操作描述
     *
     * @return
     */
    String desc() default "";
}
