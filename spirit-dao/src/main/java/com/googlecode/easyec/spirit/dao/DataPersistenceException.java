package com.googlecode.easyec.spirit.dao;

/**
 * 此异常表示在数据进行持久化时发生的异常。
 *
 * @author JunJie
 */
public class DataPersistenceException extends Exception {

    private static final long serialVersionUID = 3559738416021129355L;

    public DataPersistenceException() {
        // no op
    }

    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPersistenceException(Throwable cause) {
        super(cause);
    }
}
