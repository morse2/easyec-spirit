package com.googlecode.easyec.spirit.web.mail.mock;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.*;

/**
 * Mock类型的Java邮件发送者类。
 *
 * @author JunJie
 */
public class JavaMailMockSender implements JavaMailSender {

    private static final Logger logger = LoggerFactory.getLogger(JavaMailMockSender.class);
    private Session session;

    public synchronized Session getSession() {
        if (session == null) {
            session = Session.getDefaultInstance(new Properties());
        }

        return session;
    }

    public void send(SimpleMailMessage simpleMessage) throws MailException {
        send(new SimpleMailMessage[] { simpleMessage });
    }

    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        if (ArrayUtils.isEmpty(simpleMessages)) {
            logger.warn("Mail message objects are empty.");
            return;
        }

        for (int i = 0; i < simpleMessages.length; i++) {
            SimpleMailMessage simpleMessage = simpleMessages[i];
            if (simpleMessage == null) {
                logger.warn("Mail message object is null. index of array: [" + i + "].");
                continue;
            }

            logger.info("Printing mail message's information: [");
            logger.info("\tSubject: {" + simpleMessage.getSubject() + "}");
            logger.info("\tFrom: {" + simpleMessage.getFrom() + "}");
            logger.info("\tTo: {" + Arrays.toString(simpleMessage.getTo()) + "}");
            logger.info("\tCc: {" + Arrays.toString(simpleMessage.getCc()) + "}");
            logger.info("\tBcc: {" + Arrays.toString(simpleMessage.getBcc()) + "}");
            logger.info("\tText: {" + simpleMessage.getText() + "}");
            logger.info("].\n");
        }
    }

    public MimeMessage createMimeMessage() {
        return new MimeMessage(session);
    }

    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        try {
            return new MimeMessage(session, contentStream);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    public void send(MimeMessage mimeMessage) throws MailException {
        send(new MimeMessage[] { mimeMessage });
    }

    public void send(MimeMessage[] mimeMessages) throws MailException {
        if (ArrayUtils.isEmpty(mimeMessages)) {
            logger.warn("Mail message objects are empty.");
            return;
        }

        Map<Object, Exception> failMessages = new HashMap<Object, Exception>();

        for (int i = 0; i < mimeMessages.length; i++) {
            MimeMessage message = mimeMessages[i];
            if (message == null) {
                logger.warn("Mail message object is null. index of array: [" + i + "].");
                continue;
            }

            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                logger.info("Printing mail message's information: [");
                logger.info("\tSubject: {" + message.getSubject() + "}");
                logger.info("\tFrom: {" + Arrays.toString(message.getFrom()) + "}");
                logger.info("\tTo: {" + Arrays.toString(message.getRecipients(Message.RecipientType.TO)) + "}");
                logger.info("\tCc: {" + Arrays.toString(message.getRecipients(Message.RecipientType.CC)) + "}");
                logger.info("\tBcc: {" + Arrays.toString(message.getRecipients(Message.RecipientType.BCC)) + "}");
                logger.info("\tText: {" + message.getContent() + "}");
                logger.info("].\n");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                failMessages.put(mimeMessages, e);
            }
        }

        if (!failMessages.isEmpty()) {
            throw new MailSendException(failMessages);
        }
    }

    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        send(new MimeMessagePreparator[] { mimeMessagePreparator });
    }

    public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
        if (ArrayUtils.isNotEmpty(mimeMessagePreparators)) {
            Map<Object, Exception> failMessages = new HashMap<Object, Exception>();

            List<MimeMessage> list = new ArrayList<MimeMessage>();
            for (MimeMessagePreparator preparator : mimeMessagePreparators) {
                MimeMessage mimeMessage = createMimeMessage();

                try {
                    preparator.prepare(mimeMessage);
                    list.add(mimeMessage);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                    failMessages.put(mimeMessage, e);
                }
            }

            if (!failMessages.isEmpty()) {
                throw new MailSendException(failMessages);
            }

            send(list.toArray(new MimeMessage[list.size()]));
        }
    }
}