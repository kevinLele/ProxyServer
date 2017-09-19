package com.hq.CloudPlatform.BaseFrame.utils.validation;

import com.hq.CloudPlatform.BaseFrame.exception.ServiceException;
import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationBean;
import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationField;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 7/4/2017.
 */
public class ValidationMethodInterceptor implements MethodInterceptor {

    @Autowired
    @Lazy
    private HttpServletRequest request;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 获取调用方法时传入的参数数组
        Object[] args = invocation.getArguments();

        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = invocation.getArguments()[i];

                if (null == arg) {
                    continue;
                }

                Class cls = arg.getClass();

                //被标识为需要验证的Bean
                if (cls.isAnnotationPresent(ValidationBean.class)) {
                    Field[] fields = cls.getDeclaredFields();

                    if (null != fields && fields.length > 0) {
                        for (Field field : fields) {
                            //被标识为需要验证的属性
                            if (field.isAnnotationPresent(ValidationField.class)) {
                                ValidationField validationField = field.getDeclaredAnnotation(ValidationField.class);
                                String regex = validationField.regex();
                                String tip = validationField.tip();
                                tip = tip == null ? "" : tip;
                                boolean notNull = validationField.notNull();
                                field.setAccessible(true);
                                Object fieldValue = field.get(arg);

                                if (notNull && (null == fieldValue
                                        || "".equals(fieldValue.toString()))) {
                                    throw new ServiceException("字段" + field.getName() + "不允许为空!");
                                }

                                if (StringUtils.isNotBlank(regex)) {
                                    Pattern ptn = Pattern.compile(regex);
                                    Matcher matcher = ptn.matcher(fieldValue.toString());

                                    if (!matcher.matches()) {
                                        throw new ServiceException("字段" + field.getName() + tip + "!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return invocation.proceed();
    }
}
