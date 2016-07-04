package com.googlecode.easyec.spirit.mybatis.handlers;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * 本地化数据类型处理器类。
 * <p>
 * 此类主要用于映射Java到数据库和数据库到Java的类型。
 * </p>
 *
 * @author JunJie
 */
@MappedTypes({ Locale.class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class LocaleTypeHandler extends BaseTypeHandler<Locale> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Locale parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Locale getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return StringUtils.isNotBlank(s) ? LocaleUtils.toLocale(s) : null;
    }

    @Override
    public Locale getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return StringUtils.isNotBlank(s) ? LocaleUtils.toLocale(s) : null;
    }

    @Override
    public Locale getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return StringUtils.isNotBlank(s) ? LocaleUtils.toLocale(s) : null;
    }
}
