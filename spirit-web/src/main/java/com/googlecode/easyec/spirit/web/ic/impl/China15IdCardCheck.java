package com.googlecode.easyec.spirit.web.ic.impl;

import com.googlecode.easyec.spirit.web.ic.InvalidIdException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 该类实现了中华人民共和国15位身份证的校验算法
 *
 * @author JunJie
 */
final class China15IdCardCheck extends ChinaIdCardCheck {

    private static final String LEAP_YEAR_15 = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$";
    private static final String AVRG_YEAR_15 = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$";

    China15IdCardCheck() {
        super(LEAP_YEAR_15, AVRG_YEAR_15);
    }

    @Override
    int getYear(String id) {
        return Integer.parseInt(id.substring(6, 8)) + 1900;
    }

    @Override
    public void validate(String id, IdType type) throws InvalidIdException {
        if (isBlank(id) || type == null) {
            throw new InvalidIdException("Both ID and type cannot be null.");
        }

        if (id.length() != 15) {
            throw new InvalidIdException("ID length must be equal to 15.");
        }

        switch (type) {
            case Male:
                if ((id.charAt(14) - '0') % 2 == 0) {
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
                if ((id.charAt(14) - '0') % 2 != 0) {
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

        logger.info("ID [{}] is valid.", id);
    }
}
