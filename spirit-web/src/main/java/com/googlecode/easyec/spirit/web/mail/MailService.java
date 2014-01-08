package com.googlecode.easyec.spirit.web.mail;

import org.springframework.mail.MailException;

/**
 * 邮件服务接口类。
 * <p>
 * 此接口定义了发送邮件内容的方法。
 * </p>
 *
 * @author JunJie
 */
public interface MailService {

    /**
     * 发送邮件方法。
     *
     * @param mo 邮件内容对象
     * @throws MailException 有错误则抛出此异常
     */
    void send(MailObject mo) throws MailException;

    /**
     * 发送一组邮件的方法。
     *
     * @param mos 一组邮件内容对象
     * @throws MailException 有错误则抛出此异常
     */
    void send(MailObject[] mos) throws MailException;
}
