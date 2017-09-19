package com.hq.CloudPlatform.BaseFrame.utils.dictionary.annotation;

import java.lang.annotation.*;

/**
 * 用于标识需要对返回结果的数据字典进行转换的方法
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HasDictionaryFields {

}
