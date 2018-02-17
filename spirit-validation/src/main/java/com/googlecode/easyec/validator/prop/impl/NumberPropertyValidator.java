package com.googlecode.easyec.validator.prop.impl;

import com.googlecode.easyec.validator.ValidationException;
import com.googlecode.easyec.validator.prop.AbstractMessagingPropertyValidator;
import com.googlecode.easyec.validator.prop.Property;
import org.springframework.util.Assert;

public class NumberPropertyValidator extends AbstractMessagingPropertyValidator {

    public enum Method {

        /** 大于或等于 */
        GreaterEquals,
        /** 大于 */
        GreaterThan,
        /** 小于或等于 */
        LessEquals,
        /** 小于 */
        LessThan
    }

    private Method method;
    private Number value;

    public NumberPropertyValidator(Method method, Number value) {
        Assert.notNull(method, "Parameter 'method' cannot be null.");
        Assert.notNull(value, "Parameter 'value' cannot be null.");

        this.method = method;
        this.value = value;
    }

    public Method getMethod() {
        return method;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public void validate(Property property) throws ValidationException {
        Object value = getPropertyValue(property);
        if (value == null || !(value instanceof Number)) {
            logger.warn(
                "Neither property [{}]'s value is null or not present. Value: [{}].",
                property.getProperty(), value
            );

            return;
        }

        double v1 = getValue().doubleValue();
        double v2 = ((Number) value).doubleValue();
        switch (method) {
            case LessThan:
                if (!(v2 < v1)) _throws();

                break;
            case GreaterThan:
                if (!(v2 > v1)) _throws();

                break;
            case LessEquals:
                if (!(v2 <= v1)) _throws();

                break;
            case GreaterEquals:
                if (!(v2 >= v1)) _throws();
        }
    }

    private void _throws() throws ValidationException {
        ValidationException ex = new ValidationException(getMessage());
        ex.setLocalized(isLocalized());
        throw ex;
    }
}
