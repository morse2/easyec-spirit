package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SOAP的消息头类。
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Header {

    @XmlElementRef
    private HeadContent headContent;

    /**
     * 默认构造方法
     */
    protected Header() {
        // no op
    }

    /**
     * 构造方法
     *
     * @param headContent 消息头内容对象
     */
    protected Header(HeadContent headContent) {
        this.headContent = headContent;
    }

    /**
     * 返回消息头内容对象
     *
     * @return <code>HeadContent</code>对象
     */
    public HeadContent getHeadContent() {
        return headContent;
    }
}
