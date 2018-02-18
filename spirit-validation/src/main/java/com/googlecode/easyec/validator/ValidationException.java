package com.googlecode.easyec.validator;

public class ValidationException extends Exception {

    private static final long serialVersionUID = -8464175157899178554L;
    private boolean localized = true;
    private Object reference;

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
    public boolean isLocalized() {
        return localized;
    }

    /**
     * 设置当前异常消息是否是一个i18n的key值
     *
     * @param b 布尔值
     */
    public void setLocalized(boolean b) {
        this.localized = b;
    }
}
