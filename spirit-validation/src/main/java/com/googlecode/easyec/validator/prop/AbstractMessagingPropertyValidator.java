package com.googlecode.easyec.validator.prop;

import com.googlecode.easyec.validator.MessageCtrl;

/**
 * 支持消息化的属性验证类
 *
 * @author junjie
 */
public abstract class AbstractMessagingPropertyValidator extends AbstractPropertyValidator implements MessageCtrl {

    private String message;
    private boolean localized;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isLocalized() {
        return localized;
    }

    @Override
    public void setLocalized(boolean b) {
        this.localized = b;
    }
}
