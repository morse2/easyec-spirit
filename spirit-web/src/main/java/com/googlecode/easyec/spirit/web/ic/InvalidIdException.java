package com.googlecode.easyec.spirit.web.ic;

/**
 * 无效ID标识的异常类
 *
 * @author JunJie
 */
public class InvalidIdException extends Exception {

    private static final long serialVersionUID = 711344918055147255L;

    public InvalidIdException(String message) {
        super(message);
    }

    public InvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIdException(Throwable cause) {
        super(cause);
    }
}
