package com.googlecode.easyec.spirit.dao.paging;

/**
 * 分页异常信息类。此类表示分页时候出错的异常信息。
 *
 * @author JunJie
 */
public class PagingException extends Exception {

    private static final long serialVersionUID = -5567080872033314197L;

    public PagingException() {
        // no op
    }

    public PagingException(String message) {
        super(message);
    }

    public PagingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PagingException(Throwable cause) {
        super(cause);
    }
}
