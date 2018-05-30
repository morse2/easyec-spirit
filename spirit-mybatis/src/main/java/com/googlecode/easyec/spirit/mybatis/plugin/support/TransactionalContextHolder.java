package com.googlecode.easyec.spirit.mybatis.plugin.support;

import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public final class TransactionalContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalContextHolder.class);
    private static ThreadLocal<Transaction> _t = new ThreadLocal<>();
    private static boolean useFlag = false;

    public static void setUse(boolean b) {
        useFlag = b;
    }

    public static boolean isUse() {
        return useFlag;
    }

    public static Transaction get() {
        return _t.get();
    }

    public static void clear() {
        Transaction _tx = _t.get();
        if (_tx != null) {
            try {
                _tx.close();
            } catch (SQLException e) {
                logger.trace(e.getMessage(), e);
            } finally {
                _t.remove();
            }
        }
    }

    public static void set(Transaction tx) {
        if (tx != null) _t.set(tx);
    }
}
