package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WebService信封类。
 * 该类用于封装消息头、
 * 消息体与其他的消息内容。
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Envelope {

    @XmlElementRef
    protected Header header;
    @XmlElementRef
    protected Body   body;

    /**
     * 默认构造方法。
     */
    protected Envelope() {
        // no op
    }

    /**
     * 构造方法。
     *
     * @param body 消息体对象
     */
    protected Envelope(Body body) {
        this(null, body);
    }

    /**
     * 构造方法。
     *
     * @param header 消息头对象
     * @param body   消息体对象
     */
    protected Envelope(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    /**
     * 获取信封的消息头
     *
     * @return {@link Header}
     */
    public Header getHeader() {
        return header;
    }

    /**
     * 获取信封的消息体
     *
     * @return {@link Body}
     */
    public Body getBody() {
        return body;
    }
}
