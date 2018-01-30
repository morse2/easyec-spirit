package com.googlecode.easyec.zkoss.validator.prop.impl;

import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.prop.AbstractPropertyValidator;
import org.springframework.util.Assert;
import org.zkoss.bind.Property;

public class NumberPropertyValidator extends AbstractPropertyValidator {

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

    public NumberPropertyValidator(String message, Method method, Number value) {
        super(message);

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
        Object value = property.getValue();
        if (value == null || !(value instanceof Number)) {
            throw new ValidationException(getMessage());
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
        throw new ValidationException(getMessage());
    }
}
