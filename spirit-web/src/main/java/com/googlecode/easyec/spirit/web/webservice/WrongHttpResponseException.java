package com.googlecode.easyec.spirit.web.webservice;

/**
 * 错误的HTTP响应代码的异常类.
 * 错误的响应代码指非200的响应代码.
 *
 * @author JunJie
 */
public class WrongHttpResponseException extends Exception {

    private static final long serialVersionUID = -5879819762389258996L;
    private int code;

    public WrongHttpResponseException(int code) {
        this.code = code;
    }

    public WrongHttpResponseException(String message, int code) {
        super(message);
        this.code = code;
    }

    public WrongHttpResponseException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    /**
     * 返回HTTP响应代码
     */
    public int getCode() {
        return code;
    }
}
