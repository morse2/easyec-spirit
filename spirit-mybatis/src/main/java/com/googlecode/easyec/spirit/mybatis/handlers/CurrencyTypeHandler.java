package com.googlecode.easyec.spirit.mybatis.handlers;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;

/**
 * 货币数据类型处理器类。
 * <p>
 * 此类用于映射Java到数据库和数据库到Java之间的类型
 * </p>
 *
 * @author JunJie
 */
@MappedTypes({ Currency.class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class CurrencyTypeHandler extends BaseTypeHandler<Currency> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Currency parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCurrencyCode());
    }

    @Override
    public Currency getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return StringUtils.isNotBlank(s) ? Currency.getInstance(s) : null;
    }

    @Override
    public Currency getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return StringUtils.isNotBlank(s) ? Currency.getInstance(s) : null;
    }

    @Override
    public Currency getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return StringUtils.isNotBlank(s) ? Currency.getInstance(s) : null;
    }
}
