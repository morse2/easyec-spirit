package com.googlecode.easyec.spirit.web.mail.impl;

import com.googlecode.easyec.spirit.web.mail.MailObject;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 简单邮件内容对象
 *
 * @author JunJie
 */
public class SimpleMailObject implements MailObject {

    private static final long serialVersionUID = 7468896115911048193L;
    private String subject;
    private String content;
    private String from;
    private List<String> recipients = new ArrayList<String>();
    private List<String> ccs = new ArrayList<String>();
    private List<String> bccs = new ArrayList<String>();

    public boolean addRecipient(String recipient) {
        return isNotBlank(recipient)
            && !recipients.contains(recipient)
            && recipients.add(recipient);
    }

    public boolean addCc(String cc) {
        return isNotBlank(cc) && !ccs.contains(cc) && ccs.add(cc);
    }

    public boolean addBcc(String bcc) {
        return isNotBlank(bcc) && !bccs.contains(bcc) && bccs.add(bcc);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public List<String> getCc() {
        return ccs;
    }

    public List<String> getBcc() {
        return bccs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMailObject that = (SimpleMailObject) o;

        if (bccs != null ? !bccs.equals(that.bccs) : that.bccs != null) return false;
        if (ccs != null ? !ccs.equals(that.ccs) : that.ccs != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (recipients != null ? !recipients.equals(that.recipients) : that.recipients != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (ccs != null ? ccs.hashCode() : 0);
        result = 31 * result + (bccs != null ? bccs.hashCode() : 0);
        return result;
    }
}