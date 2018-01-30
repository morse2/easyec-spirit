package com.googlecode.easyec.zkoss.validator.prop;

import com.googlecode.easyec.zkoss.validator.ValidationException;
import org.zkoss.bind.Property;

public interface PropertyValidator {

    /**
     * 验证属性值的方法
     *
     * @param property ZK属性对象
     * @throws ValidationException 验证失败的异常信息
     */
    void validate(Property property) throws ValidationException;
}
