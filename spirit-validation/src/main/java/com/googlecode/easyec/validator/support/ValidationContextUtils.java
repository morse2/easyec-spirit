package com.googlecode.easyec.validator.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;

import static com.googlecode.easyec.validator.support.ValidationContextHolder.get;

public class ValidationContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidationContextUtils.class);

    private ValidationContextUtils() { }

    public static String getCommand() {
        return get().getCommand();
    }

    public static Object getBase() {
        return ((BeanWrapper) get().getAccessor()).getWrappedInstance();
    }

    public static Object getProperty(String property) {
        PropertyAccessor propAcc = get().getAccessor();
        if (propAcc != null) {
            boolean b = propAcc.isReadableProperty(property);
            logger.info("Has readable property? [{}]. Property: [{}].", b, property);

            return b ? propAcc.getPropertyValue(property) : null;
        }

        logger.warn("Cannot find value with property. [{}].", property);

        return null;
    }

    public static <T> T getProperty(String property, Class<T> cls) {
        Object propVal = getProperty(property);
        return propVal != null
            ? cls.cast(propVal)
            : null;
    }
}
