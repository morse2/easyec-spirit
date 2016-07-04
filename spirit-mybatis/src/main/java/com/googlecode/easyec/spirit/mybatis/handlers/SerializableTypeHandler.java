package com.googlecode.easyec.spirit.mybatis.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.math.NumberUtils.*;

/**
 * 序列化对象类型映射处理类。
 * <p>
 * 该类负责处理JDBC类型为下面类型的可序列化对象:
 * <ol>
 * <li>NUMERIC</li>
 * <li>DECIMAL</li>
 * <li>INTEGER</li>
 * <li>BIGINT</li>
 * <li>DOUBLE</li>
 * <li>FLOAT</li>
 * <li>VARCHAR</li>
 * </ol>
 * 通常该类负责处理复杂的主键字段对象。
 * </p>
 *
 * @author JunJie
 */
@MappedJdbcTypes({
    JdbcType.NUMERIC,
    JdbcType.INTEGER,
    JdbcType.DECIMAL,
    JdbcType.BIGINT,
    JdbcType.DOUBLE,
    JdbcType.FLOAT,
    JdbcType.VARCHAR
})
@MappedTypes(Serializable.class)
public class SerializableTypeHandler extends BaseTypeHandler<Serializable> {

    private static final Logger logger = LoggerFactory.getLogger(SerializableTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Serializable parameter, JdbcType jdbcType) throws SQLException {
        try {
            switch (jdbcType) {
                case NUMERIC:
                case BIGINT:
                    ps.setLong(i, castIfNecessary(parameter, Long.class, 0L));
                    break;
                case INTEGER:
                    ps.setInt(i, castIfNecessary(parameter, Integer.class, 0));
                    break;
                case DECIMAL:
                    ps.setBigDecimal(i, castIfNecessary(parameter, BigDecimal.class, null));
                    break;
                case DOUBLE:
                    ps.setDouble(i, castIfNecessary(parameter, Double.class, 0.));
                    break;
                case FLOAT:
                    ps.setFloat(i, castIfNecessary(parameter, Float.class, 0.f));
                    break;
                case VARCHAR:
                    ps.setString(i, castIfNecessary(parameter, String.class, null));
            }
        } catch (SQLException e) {
            logger.error("Some errors occurred when setting parameter." +
                " position: [" + i + "], parameter: [" + parameter + "]," +
                " jdbc type: [" + jdbcType + "].");

            throw e;
        }
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int count = md.getColumnCount();
        logger.debug("Column's count of this ResultSetMetaData. [{}].", count);

        for (int i = 1; i <= count; i++) {
            String label = md.getColumnLabel(i);
            if (isBlank(label)) label = md.getColumnName(i);

            if (columnName.equalsIgnoreCase(label)) {
                switch (JdbcType.forCode(md.getColumnType(i))) {
                    case VARCHAR:
                        return rs.getString(columnName);
                    case NUMERIC:
                    case BIGINT:
                        return rs.getLong(columnName);
                    case INTEGER:
                        return rs.getInt(columnName);
                    case DECIMAL:
                        return rs.getBigDecimal(columnName);
                    case DOUBLE:
                        return rs.getDouble(columnName);
                    case FLOAT:
                        return rs.getFloat(columnName);
                }
            }
        }

        return null;
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        JdbcType jdbcType = JdbcType.forCode(rs.getMetaData().getColumnType(columnIndex + 1));
        logger.debug("JDBC type of column index, [{}].", jdbcType);

        switch (jdbcType) {
            case BIGINT:
            case NUMERIC:
                return rs.getLong(columnIndex);
            case INTEGER:
                return rs.getInt(columnIndex);
            case DECIMAL:
                return rs.getBigDecimal(columnIndex);
            case DOUBLE:
                return rs.getDouble(columnIndex);
            case FLOAT:
                return rs.getFloat(columnIndex);
            case VARCHAR:
                return rs.getString(columnIndex);
        }

        return null;
    }

    @Override
    public Serializable getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        JdbcType jdbcType = JdbcType.forCode(cs.getMetaData().getColumnType(columnIndex + 1));
        logger.debug("JDBC type of column index, [{}].", jdbcType);

        switch (jdbcType) {
            case NUMERIC:
            case BIGINT:
                return cs.getLong(columnIndex);
            case INTEGER:
                return cs.getInt(columnIndex);
            case DECIMAL:
                return cs.getBigDecimal(columnIndex);
            case DOUBLE:
                return cs.getDouble(columnIndex);
            case FLOAT:
                return cs.getFloat(columnIndex);
            case VARCHAR:
                return cs.getString(columnIndex);
        }

        return null;
    }

    private <T> T castIfNecessary(Serializable ser, Class<T> type, T defaultValue) {
        if (ser == null) return defaultValue;
        if (type.isInstance(ser)) return type.cast(ser);

        String s = String.valueOf(ser);
        if (String.class.isAssignableFrom(type)) {
            return type.cast(s);
        }

        if (Integer.class.isAssignableFrom(type)) {
            return type.cast(createInteger(s));
        }

        if (Long.class.isAssignableFrom(type)) {
            return type.cast(createLong(s));
        }

        if (Float.class.isAssignableFrom(type)) {
            return type.cast(createFloat(s));
        }

        if (Double.class.isAssignableFrom(type)) {
            return type.cast(createDouble(s));
        }

        if (BigDecimal.class.isAssignableFrom(type)) {
            return type.cast(createBigDecimal(s));
        }

        logger.warn("Parameter cannot match the type. Actual: [{}], expect: [{}].",
            ser.getClass().getName(), type.getName());

        return null;
    }
}
