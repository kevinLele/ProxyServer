package com.hq.CloudPlatform.BaseFrame.utils.validation.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 7/4/2017.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ValidationField {

    /**
     * 是否不允许为空, 默认为false
     */
    boolean notNull() default false;

    String regex() default "";

    String tip() default "";
}
