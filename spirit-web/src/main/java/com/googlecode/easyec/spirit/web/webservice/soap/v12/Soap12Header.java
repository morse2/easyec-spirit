package com.googlecode.easyec.spirit.web.webservice.soap.v12;

import com.googlecode.easyec.spirit.web.webservice.HeadContent;
import com.googlecode.easyec.spirit.web.webservice.Header;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支持SOAP协议的消息头类
 *
 * @author JunJie
 */
@XmlRootElement(name = "Header")
public class Soap12Header extends Header {

    /**
     * 默认构造方法
     */
    private Soap12Header() {
        // no op
    }

    /**
     * 构造方法
     *
     * @param headContents 一组消息头内容对象列表
     */
    public Soap12Header(HeadContent[] headContents) {
        super(headContents);
    }
}
