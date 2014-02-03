package com.googlecode.easyec.spirit.dao.id;

/**
 * 标识ID自动生成操作将被忽略的异常类
 *
 * @author JunJie
 */
public class IgnoreIdentifierGenerateException extends RuntimeException {

    private static final long serialVersionUID = -2577646240689859535L;

    public IgnoreIdentifierGenerateException() { /* no op */ }

    public IgnoreIdentifierGenerateException(String message) {
        super(message);
    }

    public IgnoreIdentifierGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IgnoreIdentifierGenerateException(Throwable cause) {
        super(cause);
    }
}
