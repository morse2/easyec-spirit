package com.googlecode.easyec.spirit.dao.identifier.impl;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * HILO算法主键自增长处理类。
 *
 * @author JunJie
 */
public class HiloIdentifierHolder implements IdentifierHolder {

    private static final Logger logger = LoggerFactory.getLogger(HiloIdentifierHolder.class);
    private String insertSql;
    private String updateSql;
    private String selectSql;

    private long loVal;
    private long hiVal;
    private long maxLoVal;

    public HiloIdentifierHolder(long maxLoVal) {
        Assert.isTrue(maxLoVal > 0, "Max LO value must be greater than 0.");

        this.maxLoVal = maxLoVal + 1;
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

    public synchronized Serializable generate(String tableId, Connection conn) throws SQLException {
        if (loVal == 0 || loVal > maxLoVal) initialize(tableId, conn);

        try {
            return hiVal * maxLoVal + loVal;
        } finally {
            loVal++;
        }
    }

    private void initialize(String tableId, Connection conn) throws SQLException {
        int i;
        do {
            PreparedStatement selectPs = conn.prepareStatement(selectSql);
            selectPs.setString(1, tableId);

            ResultSet selectRs = selectPs.executeQuery();
            if (selectRs.next()) {
                hiVal = selectRs.getLong(1);
                if (hiVal < 0) hiVal = 0;

                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setLong(1, hiVal + 1);
                updatePs.setString(2, tableId);

                i = updatePs.executeUpdate();
                logger.debug("Effect row: [" + i + "].");
            } else {
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setString(1, tableId);
                insertPs.setLong(2, hiVal + 1);

                i = insertPs.executeUpdate();
                logger.debug("Effect row: [" + i + "].");
            }
        } while (i < 1);

        loVal = 1;
    }
}
