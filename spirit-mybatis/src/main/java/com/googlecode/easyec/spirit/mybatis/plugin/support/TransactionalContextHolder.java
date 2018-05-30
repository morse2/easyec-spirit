package com.googlecode.easyec.spirit.mybatis.plugin.support;

import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class TransactionalContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalContextHolder.class);
    private static ThreadLocal<Transaction> _t = new ThreadLocal<>();

    public static boolean has() {
        Transaction _tx = _t.get();

        try {
            return _tx != null && !_tx.getConnection().isClosed();
        } catch (SQLException e) {
            logger.trace(e.getMessage(), e);
        }

        return false;
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
