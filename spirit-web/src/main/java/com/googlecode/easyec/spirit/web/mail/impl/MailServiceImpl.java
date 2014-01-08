package com.googlecode.easyec.spirit.web.mail.impl;

import com.googlecode.easyec.spirit.web.mail.MailObject;
import com.googlecode.easyec.spirit.web.mail.MailService;
import com.googlecode.easyec.spirit.web.mail.internet.MimeMailObject;
import com.googlecode.easyec.spirit.web.mail.mock.JavaMailMockSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * 邮件服务实现类。
 *
 * @author JunJie
 */
public class MailServiceImpl implements MailService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private MailSender mailSender;
    private String     defaultFrom;

    /**
     * 设置邮件发送对象
     *
     * @param mailSender 邮件发送对象实例
     */
    public void setMailSender(MailSender mailSender) {
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

    public void send(MailObject mo) throws MailException {
        send(new MailObject[] { mo });
    }

    public void send(MailObject[] mos) throws MailException {
        if (mailSender instanceof JavaMailSender) {
            doSendMimeMessage(mos);
        } else {
            doSendSimpleMessage(mos);
        }
    }

    private void doSendSimpleMessage(MailObject[] mos) throws MailException {
        for (MailObject mo : mos) {
            SimpleMailMessage mm = new SimpleMailMessage();
            mm.setSubject(mo.getSubject());
            mm.setText(mo.getContent());

            if (StringUtils.hasText(mo.getFrom())) {
                mm.setFrom(mo.getFrom());
            } else {
                mm.setFrom(defaultFrom);
            }

            List<String> recipients = mo.getRecipients();
            mm.setTo(recipients.toArray(new String[recipients.size()]));

            List<String> ccs = mo.getCc();
            mm.setCc(ccs.toArray(new String[ccs.size()]));

            List<String> bccs = mo.getBcc();
            mm.setBcc(bccs.toArray(new String[bccs.size()]));

            mm.setSentDate(new Date());

            // 发送邮件
            mailSender.send(mm);
        }
    }

    private void doSendMimeMessage(MailObject[] mos) throws MailException {
        Map<Object, Exception> failMessages = new HashMap<Object, Exception>();

        for (MailObject mo : mos) {
            MimeMessage message = ((JavaMailSender) mailSender).createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            try {
                helper.setSubject(mo.getSubject());

                if (StringUtils.hasText(mo.getFrom())) {
                    helper.setFrom(mo.getFrom());
                } else {
                    helper.setFrom(defaultFrom);
                }

                List<String> recipients = mo.getRecipients();
                logger.debug("Recipients, {}", Arrays.toString(recipients.toArray()));
                helper.setTo(recipients.toArray(new String[recipients.size()]));

                List<String> ccs = mo.getCc();
                logger.debug("CCs, {}", Arrays.toString(ccs.toArray()));
                helper.setCc(ccs.toArray(new String[ccs.size()]));

                List<String> bccs = mo.getBcc();
                logger.debug("BCCs, {}", Arrays.toString(bccs.toArray()));
                helper.setBcc(bccs.toArray(new String[bccs.size()]));

                // add attachments and text
                if (mo instanceof MimeMailObject) {
                    Set<String> names = ((MimeMailObject) mo).getAttachmentNames();
                    for (String key : names) {
                        byte[] bs = ((MimeMailObject) mo).getAttachment(key);

                        helper.addAttachment(key, new ByteArrayResource(bs));
                    }

                    helper.setText(mo.getContent(), ((MimeMailObject) mo).isHtml());
                } else {
                    helper.setText(mo.getContent());
                }

                helper.setSentDate(new Date(System.currentTimeMillis()));

                ((JavaMailSender) mailSender).send(helper.getMimeMessage());
            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);

                failMessages.put(mo, e);
            }
        }

        if (!failMessages.isEmpty()) {
            throw new MailSendException(failMessages);
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (mailSender == null) {
            mailSender = new JavaMailMockSender();
        }
    }
}