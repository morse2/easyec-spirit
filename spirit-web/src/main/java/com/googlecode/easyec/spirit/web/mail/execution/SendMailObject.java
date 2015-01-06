package com.googlecode.easyec.spirit.web.mail.execution;

import com.googlecode.easyec.spirit.web.mail.MailObject;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发送邮件对象类。
 * 该类包含要发送的邮件内容对象，
 * 以及发送失败应该如何处理的方法。
 *
 * @author JunJie
 */
final class SendMailObject implements Serializable {

    private static final long serialVersionUID = -3417663379186540549L;
    private final AtomicInteger remainSendCount;
    private final MailObject mailObject;

    SendMailObject(final int remainingSendCount, final MailObject mailObject) {
        this.remainSendCount = new AtomicInteger(remainingSendCount);
        this.mailObject = mailObject;
    }

    /**
     * 标记是否允许重新发送邮件内容
     */
    public boolean canResend() {
        return remainSendCount.getAndDecrement() > 0;
    }

    /**
     * 返回当前邮件内容对象
     */
    public MailObject getMailObject() {
        return mailObject;
    }
}
