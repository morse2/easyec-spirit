package com.googlecode.easyec.validator.prop;

import com.googlecode.easyec.validator.MessageCtrl;

/**
 * 支持消息化的属性验证类
 *
 * @author junjie
 */
public abstract class AbstractMessagingPropertyValidator extends AbstractPropertyValidator implements MessageCtrl {

    private String message;
    private boolean i18n;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isI18n() {
        return i18n;
    }

    @Override
    public void setI18n(boolean b) {
        this.i18n = b;
    }
}
