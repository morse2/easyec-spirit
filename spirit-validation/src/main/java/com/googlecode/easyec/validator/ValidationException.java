package com.googlecode.easyec.validator;

public class ValidationException extends Exception {

    private static final long serialVersionUID = -6047124095930189011L;
    private Object reference;
    private boolean i18nMsg;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    /**
     * 判断异常消息是否为i18n的key值
     *
     * @return 布尔值
     */
    public boolean isI18nMsg() {
        return i18nMsg;
    }

    /**
     * 设置当前异常消息是否是一个i18n的key值
     *
     * @param b 布尔值
     */
    public void setI18nMsg(boolean b) {
        this.i18nMsg = b;
    }
}
