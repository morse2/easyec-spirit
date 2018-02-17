package com.googlecode.easyec.validator.prop.impl;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * 中国地区固话号码格式的验证器实现类
 *
 * @author junjie
 */
public class ChinaTelephonePropertyValidator extends PhonePropertyValidator {

    private static Pattern _pTelephone
        = Pattern.compile("^(0\\\\d{2}-\\\\d{8}(-\\\\d{1,4})?)|(0\\\\d{3}-\\\\d{7,8}(-\\\\d{1,4})?)$", CASE_INSENSITIVE);

    @Override
    protected Pattern getPhonePattern() {
        return _pTelephone;
    }
}
