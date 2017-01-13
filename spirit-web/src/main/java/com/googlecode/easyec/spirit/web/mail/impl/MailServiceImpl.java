package com.googlecode.easyec.spirit.web.mail.impl;

import com.googlecode.easyec.spirit.web.mail.MailCallbackService;
import com.googlecode.easyec.spirit.web.mail.MailObject;
import com.googlecode.easyec.spirit.web.mail.MailService;
import com.googlecode.easyec.spirit.web.mail.internet.MimeMailObject;
import com.googlecode.easyec.spirit.web.mail.mock.JavaMailMockSender;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.*;

/**
 * 邮件服务实现类。
 *
 * @author JunJie
 */
public class MailServiceImpl implements MailService, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final Charset UTF_8 = Charset.forName("UTF-8");

    protected JavaMailSender mailSender;
    protected String defaultFrom;

    // 回调接口类
    private MailCallbackService callbackService;

    /**
     * 设置邮件发送对象
     *
     * @param mailSender 邮件发送对象实例
     */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 设置邮件默认的发送者信息
     *
     * @param defaultFrom 邮件地址
     */
    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    /**
     * 设置邮件回调的业务接口类
     *
     * @param callbackService <code>MailCallbackService</code>
     */
    public void setCallbackService(MailCallbackService callbackService) {
        this.callbackService = callbackService;
    }

    public void send(MailObject mo) throws MailException {
        send(new MailObject[] { mo });
    }

    public void send(MailObject[] mos) throws MailException {
        _doSend(mos);
    }

    /**
     * 执行发送邮件的方法
     *
     * @param list 邮件对象内容的列表
     * @throws MailException 发送时发生异常的信息类
     */
    private void _doSend(MailObject[] list) throws MailException {
        if (ArrayUtils.isEmpty(list)) {
            logger.warn("No any MailObject was present.");

            return;
        }

        Map<Object, Exception> failMessages = new HashMap<Object, Exception>();

        for (MailObject mo : list) {
            try {
                mailSender.send(
                    _createMessageHelper(mo).getMimeMessage()
                );

                if (callbackService != null) {
                    callbackService.afterSent(mo);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                failMessages.put(mo, e);

                if (callbackService != null) {
                    callbackService.afterThrowing(mo);
                }
            }
        }

        if (!failMessages.isEmpty()) {
            throw new MailSendException(failMessages);
        }
    }

    private MimeMessageHelper _createMessageHelper(MailObject mo) throws Exception {
        List<String> recipients = mo.getRecipients();
        Assert.isTrue(
            CollectionUtils.isNotEmpty(recipients),
            "No any recipients was present."
        );

        MimeMessageHelper helper = new MimeMessageHelper(
            mailSender.createMimeMessage(), UTF_8.name()
        );

        helper.setSubject(mo.getSubject());
        helper.setFrom(
            StringUtils.hasText(mo.getFrom())
                ? mo.getFrom()
                : defaultFrom
        );

        if (CollectionUtils.isNotEmpty(recipients)) {
            logger.debug("Recipients: [{}]", Arrays.toString(recipients.toArray()));
            helper.setTo(recipients.toArray(new String[recipients.size()]));
        }

        List<String> ccs = mo.getCc();
        if (CollectionUtils.isNotEmpty(ccs)) {
            logger.debug("CCs: [{}]", Arrays.toString(ccs.toArray()));
            helper.setCc(ccs.toArray(new String[ccs.size()]));
        }

        List<String> bccs = mo.getBcc();
        if (CollectionUtils.isNotEmpty(bccs)) {
            logger.debug("BCCs: [{}]", Arrays.toString(bccs.toArray()));
            helper.setBcc(bccs.toArray(new String[bccs.size()]));
        }

        helper.setText(mo.getContent(),
            (mo instanceof MimeMailObject)
                && ((MimeMailObject) mo).isHtml()
        );

        helper.setSentDate(new Date());

        // 添加MIME类型邮件的附件信息
        if (mo instanceof MimeMailObject) {
            Set<String> names = ((MimeMailObject) mo).getAttachmentNames();
            for (String key : names) {
                byte[] bs = ((MimeMailObject) mo).getAttachment(key);
                helper.addAttachment(key, new ByteArrayResource(bs));
            }
        }

        return helper;
    }

    public void afterPropertiesSet() throws Exception {
        if (mailSender == null) {
            mailSender = new JavaMailMockSender();
        }
    }
}