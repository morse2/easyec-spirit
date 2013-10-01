package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 信封错误体消息类
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Fault {

    @XmlElementRef
    private FaultContent faultContent;

    /**
     * 默认构造方法
     */
    protected Fault() {
        // no op
    }

    /**
     * 返回错误体消息内容对象
     *
     * @return <code>FaultContent</code>对象
     */
    public FaultContent getFaultContent() {
        return faultContent;
    }
}
