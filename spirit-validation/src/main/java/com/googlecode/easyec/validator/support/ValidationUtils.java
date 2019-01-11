package com.googlecode.easyec.validator.support;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ValidationUtils {

    private ValidationUtils() {}

    public static Object getProperty(String property) {
        Object bean = ValidationContextHolder.get();
        if (bean != null) {
            BeanWrapper bw = new BeanWrapperImpl(bean);
            if (bw.isReadableProperty(property)) {
                return bw.getPropertyValue(property);
            }
        }

        return null;
    }
}
