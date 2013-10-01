package com.googlecode.easyec.spirit.web.webservice.soap.v11;

import com.googlecode.easyec.spirit.web.webservice.HeadContent;
import com.googlecode.easyec.spirit.web.webservice.Header;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支持SOAP协议的消息头类
 *
 * @author JunJie
 */
@XmlRootElement(name = "Header")
public class Soap11Header extends Header {

    /**
     * 默认构造方法
     */
    private Soap11Header() {
        // no op
    }

    /**
     * 构造方法
     *
     * @param headContent 消息头内容对象
     */
    public Soap11Header(HeadContent headContent) {
        super(headContent);
    }
}
