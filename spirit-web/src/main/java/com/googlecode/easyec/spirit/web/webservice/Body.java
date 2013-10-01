package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 信封的消息体类。
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Body {

    @XmlElementRef
    private BodyContent bodyContent;
    @XmlElementRef
    private Fault       fault;

    /**
     * 默认构造方法
     */
    protected Body() {}

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     */
    protected Body(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     * @param fault       消息体错误对象
     */
    protected Body(BodyContent bodyContent, Fault fault) {
        this.bodyContent = bodyContent;
        this.fault = fault;
    }

    /**
     * 返回消息体内容对象
     *
     * @return <code>BodyContent</code>对象
     */
    public BodyContent getBodyContent() {
        return bodyContent;
    }

    /**
     * 获取消息错误体
     *
     * @return <code>Fault</code>对象
     */
    Fault getFault() {
        return fault;
    }
}
