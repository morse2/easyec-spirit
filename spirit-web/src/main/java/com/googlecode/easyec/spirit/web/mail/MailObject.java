package com.googlecode.easyec.spirit.web.mail;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件载体对象类。
 * <p>
 * 该类表现了一个邮件应该有的基本信息。
 * 例如：发送者、接收者、主题、正文内容
 * </p>
 *
 * @author JunJie
 */
public interface MailObject extends Serializable {

    /**
     * 添加一个邮件的接收者
     *
     * @param recipient 邮件接收者，邮件地址
     * @return 添加成功返回真
     */
    boolean addRecipient(String recipient);

    /**
     * 添加一个邮件的抄送者
     *
     * @param cc 邮件抄送者，邮件地址
     * @return 添加成功返回真
     */
    boolean addCc(String cc);

    /**
     * 添加一个邮件的暗抄者
     *
     * @param bcc 邮件暗抄者，邮件地址
     * @return 添加成功返回真
     */
    boolean addBcc(String bcc);

    /**
     * 返回当前邮件的主题
     *
     * @return 邮件主题
     */
    String getSubject();

    /**
     * 设置当前邮件的主题
     *
     * @param subject 邮件主题
     */
    void setSubject(String subject);

    /**
     * 返回当前邮件的正文内容
     *
     * @return 邮件正文
     */
    String getContent();

    /**
     * 设置当前邮件的正文内容
     *
     * @param content 邮件正文
     */
    void setContent(String content);

    /**
     * 返回当前邮件的发送者
     *
     * @return 发送者，邮件地址
     */
    String getFrom();

    /**
     * 设置当前邮件的发送者
     *
     * @param from 邮件地址
     */
    void setFrom(String from);

    /**
     * 返回当前邮件的接收者列表
     *
     * @return 一组邮件地址
     */
    List<String> getRecipients();

    /**
     * 返回当前邮件的抄送者列表
     *
     * @return 一组邮件地址
     */
    List<String> getCc();

    /**
     * 返回当前邮件的暗抄者列表
     *
     * @return 一组邮件地址
     */
    List<String> getBcc();
}
