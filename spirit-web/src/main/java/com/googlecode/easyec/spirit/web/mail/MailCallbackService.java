package com.googlecode.easyec.spirit.web.mail;

/**
 * 邮件回调的业务类。
 * 该类定义了在处理邮件发送
 * 的过程时候，需要回调业务逻辑
 * 的方法的接口类。
 *
 * @author JunJie
 */
public interface MailCallbackService {

    /**
     * 在邮件发送之后会回调的接口方法
     *
     * @param mo 邮件对象
     */
    void afterSent(MailObject mo);

    /**
     * 在处理邮件内容或发送邮件失败之后，
     * 回调的接口方法
     *
     * @param mo 邮件对象
     */
    void afterThrowing(MailObject mo);
}
