package com.googlecode.easyec.spirit.dao.id;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 主键标识生成器类。
 * <p>
 * 该类主要负责主键生成的逻辑操作
 * </p>
 *
 * @author JunJie
 */
public interface IdentifierGenerator {

    /**
     * 判断给定的参数类型是否是当前主键标识生成器对象
     * 可以接受的类型范围。如果返回真，则方法
     * {@link #generate(String, Connection)}
     * 即被调用，以生成新的主键值
     *
     * @param idType 主键类型
     * @return 真或假
     */
    boolean accept(Class<?> idType);

    /**
     * 新序列值的生成方法。
     *
     * @param sequenceName 主键标识名称
     * @param conn         数据库连接实例
     * @return 新的序列值
     * @throws SQLException
     */
    Serializable generate(String sequenceName, Connection conn) throws SQLException;
}
