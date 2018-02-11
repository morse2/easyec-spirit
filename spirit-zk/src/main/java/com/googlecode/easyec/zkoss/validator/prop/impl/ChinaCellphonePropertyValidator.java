package com.googlecode.easyec.zkoss.validator.prop.impl;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * 中国地区手机号码格式的验证器实现类
 *
 * @author junjie
 */
public class ChinaCellphonePropertyValidator extends PhonePropertyValidator {

    private static Pattern _pCellphone
        = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$", CASE_INSENSITIVE);

    public ChinaCellphonePropertyValidator(String message) {
        super(message);
    }

    @Override
    protected Pattern getPhonePattern() {
        return _pCellphone;
    }
}
