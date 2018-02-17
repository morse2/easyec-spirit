package com.googlecode.easyec.validator.prop;

import com.googlecode.easyec.validator.ValidationException;

public interface PropertyValidator {

    /**
     * 验证属性值的方法
     *
     * @param property 属性对象
     * @throws ValidationException 验证失败的异常信息
     */
    void validate(Property property) throws ValidationException;
}
