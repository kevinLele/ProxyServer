package com.hq.CloudPlatform.BaseFrame.utils.dictionary;

import com.alibaba.fastjson.JSONObject;
import com.hq.CloudPlatform.BaseFrame.restful.view.Page;
import com.hq.CloudPlatform.BaseFrame.service.IDictionaryService;
import com.hq.CloudPlatform.BaseFrame.utils.dictionary.annotation.DictionaryField;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 7/4/2017.
 */
@Slf4j
public class DictionaryTransitionInterceptor implements MethodInterceptor {

    @Autowired
    @Lazy
    private HttpServletRequest request;

    @Autowired
    @Lazy
    private IDictionaryService dictionaryService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.debug("DictionaryTransitionInterceptor invoke: " + invocation.getMethod());
        Object result = invocation.proceed();

        if (null != result) {
            Class returnType = result.getClass();

            //遍历各个字段, 如果有数据字典注解则进行转换,将数据字典的名称设置为字段值
            if (List.class.isAssignableFrom(returnType)) {
                List list = (List) result;

                list.forEach(record -> {
                    transform(record);
                });
            } else if (Page.class.isAssignableFrom(returnType)) {
                Page page = (Page) result;
                List list = page.getRows();

                list.forEach(record -> {
                    transform(record);
                });
            } else {
                transform(result);
            }
        }

        return result;
    }

    /**
     * 对对象的数据字典类型的属性进行转换,通过字典code值将字典名称设置到属性中
     *
     * @param obj
     */
    private void transform(Object obj) {
        if (null != obj) {
            Field[] fields = obj.getClass().getDeclaredFields();

            if (null != fields && fields.length > 0) {
                //遍历所有属性
                for (Field field : fields) {
                    //如果该属性被标识为DictionaryFiled, 说明需要进行数据字典转换处理
                    if (field.isAnnotationPresent(DictionaryField.class)) {
                        try {
                            //如果是String类型的属性则直接进行转换
                            if (field.getType().isAssignableFrom(String.class)) {
                                transformField(field, obj);
                            } else { //Bean类型的属性则需要对该bean的所有属性进行遍历,逐个属性进行转换处理
                                field.setAccessible(true);
                                Object bean = field.get(obj);

                                if (null != bean) {
                                    transform(bean);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error("数据字典转换失败", e);
                        }
                    }
                }
            }
        }
    }

    private void transformField(Field field, Object obj) {
        //标识为需要进行数据字典转换的属性
        DictionaryField dictionaryField = field.getDeclaredAnnotation(DictionaryField.class);
        String parentCode = dictionaryField.value();

        //设置为允许访问私有属性
        field.setAccessible(true);
        String fieldValue;

        try {
            fieldValue = null == field.get(obj) ? "" : String.valueOf(field.get(obj));
        } catch (IllegalAccessException e) {
            log.error("读取属性值发生异常!", e);
            return;
        }

        if (StringUtils.isNotBlank(parentCode) && StringUtils.isNotBlank(fieldValue)) {
            StringBuffer values = new StringBuffer();

            //有逗号说明该属性可能是由多个字典组成的
            if (fieldValue.contains(",")) {
                String[] dictCodes = fieldValue.split(",");

                for (String dictCode : dictCodes) {
                    if (StringUtils.isNotBlank(dictCode)) {
                        values.append(getDictionaryValue(parentCode, dictCode)).append(",");
                    }
                }

                //去掉最后一个多余的逗号
                if (values.length() > 0) {
                    values.deleteCharAt(values.length() - 1);
                }
            } else {
                values.append(getDictionaryValue(parentCode, fieldValue));
            }

            //将转换后的值设置到该属性中
            try {
                field.set(obj, values.toString());
            } catch (IllegalAccessException e) {
                log.error("设置属性值发生异常!", e);
            }
        }
    }

    private String getDictionaryValue(String parentCode, String dictCode) {
        //参数不符合要求则直接返回原dictCode,不进行转换
        if (StringUtils.isBlank(parentCode) || StringUtils.isBlank(dictCode)) {
            return dictCode;
        }

        JSONObject dicJson = dictionaryService.getDictionaryByCode(parentCode.trim(), dictCode.trim());

        //存在该数据字典信息
        if (null != dicJson) {
            String dicName = dicJson.getString("name");

            if (StringUtils.isNotBlank(dicName)) {
                return dicName;
            } else {
                //该字典未设置name值,直接返回原值
                return dictCode;
            }
        } else {
            //未找到字典的信息则不进行转换处理,直接返回原值
            return dictCode;
        }
    }
}
