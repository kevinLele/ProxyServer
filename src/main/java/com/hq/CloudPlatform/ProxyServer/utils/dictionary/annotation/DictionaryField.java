package com.hq.CloudPlatform.ProxyServer.utils.dictionary.annotation;

import java.lang.annotation.*;

/**
 * 将类的属性标识为数据字典字段, 被标识为数据字典的字段会被切面转换成数据字典的名称
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DictionaryField {

    /**
     * 父数据字典的Code值
     *
     * @return
     */
    String value() default "";
}
