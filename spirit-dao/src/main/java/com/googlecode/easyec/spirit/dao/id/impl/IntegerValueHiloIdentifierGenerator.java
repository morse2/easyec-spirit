package com.googlecode.easyec.spirit.dao.id.impl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 基于HILO算法的整型主键类型值的自增长处理类
 *
 * @author JunJie
 */
public class IntegerValueHiloIdentifierGenerator extends HiloIdentifierGenerator<Integer> {

    public IntegerValueHiloIdentifierGenerator() { /* no op */ }

    public IntegerValueHiloIdentifierGenerator(long maxLoVal) {
        super(maxLoVal);
    }

    @Override
    public Integer generateNumber(String sequenceName, Connection conn) throws SQLException {
        return Integer.parseInt(String.valueOf(generateLongValue(sequenceName, conn)));
    }

    public boolean accept(Class<?> idType) {
        return null != idType && Integer.class.isAssignableFrom(idType);
    }
}
