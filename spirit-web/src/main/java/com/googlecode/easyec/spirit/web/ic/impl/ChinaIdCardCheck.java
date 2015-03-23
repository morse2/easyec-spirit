package com.googlecode.easyec.spirit.web.ic.impl;

import com.googlecode.easyec.spirit.web.ic.IdCard;
import com.googlecode.easyec.spirit.web.ic.InvalidIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * 抽象类，该类实现了中华人民共和国
 * 身份证验证的通用算法
 *
 * @author JunJie
 */
abstract class ChinaIdCardCheck implements IdCard {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String regexForLeapYear;
    private String regexForAvrgYear;

    ChinaIdCardCheck(String regexForLeapYear, String regexForAvrgYear) {
        Assert.notNull(regexForLeapYear);
        Assert.notNull(regexForAvrgYear);

        this.regexForLeapYear = regexForLeapYear;
        this.regexForAvrgYear = regexForAvrgYear;
    }

    /**
     * 获取身份证的校验和信息
     *
     * @param id 身份证号码
     */
    protected char getChecksum(String id) {
        int i = (id.charAt(0) - '0' + (id.charAt(10) - '0')) * 7
            + (id.charAt(1) - '0' + (id.charAt(11) - '0')) * 9
            + (id.charAt(2) - '0' + (id.charAt(12) - '0')) * 10
            + (id.charAt(3) - '0' + (id.charAt(13) - '0')) * 5
            + (id.charAt(4) - '0' + (id.charAt(14) - '0')) * 8
            + (id.charAt(5) - '0' + (id.charAt(15) - '0')) * 4
            + (id.charAt(6) - '0' + (id.charAt(16) - '0')) * 2
            + (id.charAt(7) - '0') + (id.charAt(8) - '0') * 6
            + (id.charAt(9) - '0') * 3;

        return "10X98765432".charAt(i % 11);
    }

    /**
     * 检查身份证格式的正确性
     *
     * @param id 身份证号码
     * @throws InvalidIdException
     */
    protected void checkIdPattern(String id) throws InvalidIdException {
        Pattern pattern = Pattern.compile(_getRegexPattern(getYear(id)));
        boolean b = pattern.matcher(id).matches();
        logger.debug("Is pattern of ID card valid? [{}], [{}]", id, b);

        if (!b) throw new InvalidIdException(
            new StringBuffer()
                .append("ID card is invalid. ID: [")
                .append(id).append("].")
                .toString()
        );
    }

    /**
     * 通过身份证号码获取到年份信息
     *
     * @param id 身份证号码
     */
    abstract int getYear(String id);

    /* 通过年份来获取正则表达式 */
    private String _getRegexPattern(int year) {
        if ((year % 4 == 0) || ((year % 100 == 0) && (year % 4 == 0))) {
            return regexForLeapYear;
        }

        return regexForAvrgYear;
    }
}
