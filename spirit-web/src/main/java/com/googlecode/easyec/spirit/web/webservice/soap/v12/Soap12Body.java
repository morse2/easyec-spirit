package com.googlecode.easyec.spirit.web.webservice.soap.v12;

import com.googlecode.easyec.spirit.web.webservice.Body;
import com.googlecode.easyec.spirit.web.webservice.BodyContent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支持SOAP的消息体实现类。
 *
 * @author JunJie
 */
@XmlRootElement(name = "Body")
public class Soap12Body extends Body {

    private Soap12Body() { }

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     */
    public Soap12Body(BodyContent bodyContent) {
        super(bodyContent);
    }

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     * @param fault       消息体错误对象
     */
    public Soap12Body(BodyContent bodyContent, Soap12Fault fault) {
        super(bodyContent, fault);
    }
}
