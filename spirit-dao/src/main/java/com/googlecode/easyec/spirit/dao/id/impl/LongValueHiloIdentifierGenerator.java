package com.googlecode.easyec.spirit.dao.id.impl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 基于HILO算法的长整型主键类型值的自增长处理类
 *
 * @author JunJie
 */
public class LongValueHiloIdentifierGenerator extends HiloIdentifierGenerator<Long> {

    public LongValueHiloIdentifierGenerator() { /* no op */ }

    public LongValueHiloIdentifierGenerator(long maxLoVal) {
        super(maxLoVal);
    }

    @Override
    public Long generateNumber(String sequenceName, Connection conn) throws SQLException {
        return generateLongValue(sequenceName, conn);
    }

    public boolean accept(Class<?> idType) {
        return null != idType && Long.class.isAssignableFrom(idType);
    }
}
