package com.googlecode.easyec.spirit.dao.dialect.impl;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;

import java.util.List;

/**
 * 无任何SQL实现的方言类
 *
 * @author JunJie
 */
public class NoOpPageDialect implements PageDialect {

    @Override
    public String getCountSql(String jdbcSql) {
        throw new UnsupportedOperationException("There has no any implementations.");
    }

    @Override
    public String getPagedSql(String jdbcSql) {
        throw new UnsupportedOperationException("There has no any implementations.");
    }

    @Override
    public int[] getPagedParameters(int currentPage, int pageSize) {
        throw new UnsupportedOperationException("There has no any implementations.");
    }

    @Override
    public String getDialectName() {
        throw new UnsupportedOperationException("There has no any implementations.");
    }

    @Override
    public String getSortedSql(String jdbcSql, List<String> sorts) {
        throw new UnsupportedOperationException("There has no any implementations.");
    }
}
