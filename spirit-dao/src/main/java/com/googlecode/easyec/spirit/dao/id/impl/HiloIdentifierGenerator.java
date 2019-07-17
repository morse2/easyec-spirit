package com.googlecode.easyec.spirit.dao.id.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 基于HILO算法的主键自增长处理类。
 *
 * @author JunJie
 */
public abstract class HiloIdentifierGenerator<T extends Number> implements IdentifierGenerator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConcurrentMap<String, HiloIdentifierHolder> pools = new ConcurrentHashMap<>();

    public static final String DEFAULT_INSERT_SQL = "insert into SEQUENCE_GENERATOR (id_name, id_value) values (?, ?)";
    public static final String DEFAULT_UPDATE_SQL = "update SEQUENCE_GENERATOR set id_value = ? where id_name = ? and id_value = ?";
    public static final String DEFAULT_SELECT_SQL = "select id_value from SEQUENCE_GENERATOR where id_name = ?";

    private String insertSql = DEFAULT_INSERT_SQL;
    private String updateSql = DEFAULT_UPDATE_SQL;
    private String selectSql = DEFAULT_SELECT_SQL;

    private long maxLoVal;

    protected HiloIdentifierGenerator() { /* no op */ }

    protected HiloIdentifierGenerator(long maxLoVal) {
        this.maxLoVal = maxLoVal;
    }

    public void setInsertSql(String insertSql) {
        Assert.isTrue(isNotBlank(insertSql), "Insert SQL mustn't be null.");

        this.insertSql = insertSql;
    }

    public void setUpdateSql(String updateSql) {
        Assert.isTrue(isNotBlank(updateSql), "Update SQL mustn't be null.");

        this.updateSql = updateSql;
    }

    public void setSelectSql(String selectSql) {
        Assert.isTrue(isNotBlank(selectSql), "Select SQL mustn't be null.");

        this.selectSql = selectSql;
    }

    public void setMaxLoVal(long maxLoVal) {
        this.maxLoVal = maxLoVal;
    }

    public final Serializable generate(String sequenceName, Connection conn) throws SQLException {
        return generateNumber(sequenceName, conn);
    }

    /**
     * 生成一个新的数字类型的主键值
     *
     * @param sequenceName 主键标识名称
     * @param conn         数据库连接实例
     * @return 新的序列值
     * @throws SQLException
     */
    abstract public T generateNumber(String sequenceName, Connection conn) throws SQLException;

    /**
     * 生成一个新的长整型的主键值
     *
     * @param sequenceName 主键标识名称
     * @param conn         数据库连接实例
     * @return 新的序列值
     * @throws SQLException <code>java.sql.SQLException</code>
     */
    protected long generateLongValue(String sequenceName, Connection conn) throws SQLException {
        synchronized (this) {
            return pools.getOrDefault(sequenceName,
                new HiloIdentifierHolder(maxLoVal))
                .generate(sequenceName, conn);
        }
    }

    /**
     * 基于HILO算法的主键递增的句柄类
     */
    private class HiloIdentifierHolder implements Serializable {

        private static final long serialVersionUID = -5200407217696712189L;
        private long loVal;
        private long hiVal;
        private long maxLoVal;

        HiloIdentifierHolder(long maxLoVal) {
            Assert.isTrue(maxLoVal > 0, "Max LO value must be greater than 0.");

            this.maxLoVal = maxLoVal + 1;
        }

        long generate(String idName, Connection conn) throws SQLException {
            if (loVal == 0 || loVal > maxLoVal) initialize(idName, conn);

            try {
                return hiVal * maxLoVal + loVal;
            } finally {
                loVal++;
            }
        }

        private void initialize(String idName, Connection conn) throws SQLException {
            int i;

            do {
                PreparedStatement selectPs = conn.prepareStatement(selectSql);
                selectPs.setString(1, idName);

                ResultSet selectRs = selectPs.executeQuery();
                if (selectRs.next()) {
                    hiVal = selectRs.getLong(1);
                    if (hiVal < 0) hiVal = 0;

                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setLong(1, hiVal + 1);
                    updatePs.setString(2, idName);
                    updatePs.setLong(3, hiVal);

                    i = updatePs.executeUpdate();
                    logger.debug("Effect row for updating sequence:[{}], id_name: [{}].", i, idName);
                } else {
                    PreparedStatement insertPs = conn.prepareStatement(insertSql);
                    insertPs.setString(1, idName);
                    insertPs.setLong(2, hiVal + 1);

                    i = insertPs.executeUpdate();
                    logger.debug("Effect row: for updating sequence:[{}], id_name: [{}].", i, idName);
                }
            } while (i < 1);

            loVal = 1;
        }
    }
}
