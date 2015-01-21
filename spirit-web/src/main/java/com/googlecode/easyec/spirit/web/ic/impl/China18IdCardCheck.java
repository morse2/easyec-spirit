package com.googlecode.easyec.spirit.web.ic.impl;

import com.googlecode.easyec.spirit.web.ic.InvalidIdException;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * 该类实现了中华人民共和国18位身份证的校验算法
 *
 * @author JunJie
 */
final class China18IdCardCheck extends ChinaIdCardCheck {

    private static final String LEAP_YEAR_18 = "^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$";
    private static final String AVRG_YEAR_18 = "^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$";

    China18IdCardCheck() {
        super(LEAP_YEAR_18, AVRG_YEAR_18);
    }

    @Override
    int getYear(String id) {
        return Integer.parseInt(id.substring(6, 10));
    }

    @Override
    public void validate(String id, IdType type) throws InvalidIdException {
        if (isBlank(id) || type == null) {
            throw new InvalidIdException("Both ID and type cannot be null.");
        }

        if (id.length() != 18) {
            throw new InvalidIdException("ID length must be equal to 18.");
        }

        switch (type) {
            case Male:
                if ((id.charAt(16) - '0') % 2 == 0) {
                    String err = new StringBuffer()
                        .append("ID cannot match to type. ID: [")
                        .append(id).append("], type: [")
                        .append(type).append("].")
                        .toString();
                    logger.warn(err);

                    throw new InvalidIdException(err);
                }

                break;
            case Female:
                if ((id.charAt(16) - '0') % 2 != 0) {
                    String err = new StringBuffer()
                        .append("ID cannot match to type. ID: [")
                        .append(id).append("], type: [")
                        .append(type).append("].")
                        .toString();
                    logger.warn(err);

                    throw new InvalidIdException(err);
                }
        }

        checkIdPattern(id);

        char expect = getChecksum(id);
        char actual = id.charAt(17);
        if (actual != expect) {
            String err = new StringBuffer()
                .append("Wrong checksum value, expect: [")
                .append(expect).append("], actual: [")
                .append(actual).append("].")
                .toString();
            logger.warn(err);

            throw new InvalidIdException(err);
        }

        logger.info("ID [{}] is valid.", id);
    }
}
