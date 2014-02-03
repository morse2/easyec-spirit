package com.googlecode.easyec.spirit.dao.id.impl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 基于HILO算法的短整型主键类型值的自增长处理类
 *
 * @author JunJie
 */
public class ShortValueHiloIdentifierGenerator extends HiloIdentifierGenerator<Short> {

    public ShortValueHiloIdentifierGenerator() { /* no op */ }

    public ShortValueHiloIdentifierGenerator(long maxLoVal) {
        super(maxLoVal);
    }

    @Override
    public Short generateNumber(String sequenceName, Connection conn) throws SQLException {
        return Short.parseShort(String.valueOf(generateLongValue(sequenceName, conn)));
    }

    public boolean accept(Class<?> idType) {
        return null != idType && Short.class.isAssignableFrom(idType);
    }
}
