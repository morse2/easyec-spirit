package com.googlecode.easyec.spirit.web.mail.internet;

import com.googlecode.easyec.spirit.web.mail.MailObject;

import java.io.IOException;
import java.util.Set;

/**
 * MIME类型的邮件载体对象
 *
 * @author JunJie
 */
public interface MimeMailObject extends MailObject {

    /**
     * 添加一个附件信息
     *
     * @param filename 附件名称
     * @param bs       附件的数据流信息
     * @throws IOException
     */
    void addAttachment(String filename, byte[] bs) throws IOException;

    /**
     * 设置邮件正文内容
     *
     * @param text 邮件正文
     * @param html 标识正文是否为HTML格式的内容
     */
    void setContent(String text, boolean html);

    /**
     * 根据附件名称，返回附件的数据流信息
     *
     * @param filename 附件名称
     * @return 附件内容
     */
    byte[] getAttachment(String filename);

    /**
     * 返回附件的名称列表
     *
     * @return 一组附件名称
     */
    Set<String> getAttachmentNames();

    /**
     * 返回邮件正文是否为HTML格式的内容
     *
     * @return 真表示邮件正文为HTML，假表示为明文
     */
    boolean isHtml();
}