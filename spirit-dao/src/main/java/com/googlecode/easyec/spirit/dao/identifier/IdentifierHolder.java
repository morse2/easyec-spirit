package com.googlecode.easyec.spirit.dao.identifier;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 主键标识句柄类。
 * 此类用于生成一个新的序列值。
 *
 * @author JunJie
 */
public interface IdentifierHolder {

    /**
     * 新序列值的生成方法。
     *
     * @param tableId 主键标识名称
     * @param conn    数据库连接实例
     * @return 新的序列值
     * @throws SQLException
     */
    Serializable generate(String tableId, Connection conn) throws SQLException;
}
