package com.googlecode.easyec.spirit.web.mail.internet;

import com.googlecode.easyec.spirit.web.mail.impl.SimpleMailObject;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.lang.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class MimeMailObjectImpl extends SimpleMailObject implements MimeMailObject {

    private boolean html;
    private String  htmlText;

    private ConcurrentMap<String, byte[]> attachments
        = new ConcurrentHashMap<String, byte[]>();

    public void addAttachment(String filename, byte[] bs) throws IOException {
        if (isNotBlank(filename) && isNotEmpty(bs)) {
            attachments.put(filename, bs);
        }
    }

    public Set<String> getAttachmentNames() {
        return attachments.keySet();
    }

    public byte[] getAttachment(String filename) {
        return isNotBlank(filename) && attachments.containsKey(filename)
            ? attachments.get(filename)
            : new byte[0];
    }

    public void setContent(String text, boolean html) {
        if (html) this.htmlText = text;
        else setContent(text);

        this.html = html;
    }

    public boolean isHtml() {
        return html;
    }

    @Override
    public String getContent() {
        return html ? htmlText : super.getContent();
    }
}