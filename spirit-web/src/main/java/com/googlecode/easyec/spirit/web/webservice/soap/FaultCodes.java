package com.googlecode.easyec.spirit.web.webservice.soap;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * 错误消息枚举信息类
 *
 * @author JunJie
 */
@XmlEnum
public enum FaultCodes {

    /**
     * SOAP Envelope 元素的无效命名空间被发现
     */
    @XmlEnumValue("VersionMismatch")
    VERSION_MISMATCH,
    /**
     * Header 元素的一个直接子元素（带有设置为 "1" 的 mustUnderstand 属性）无法被理解
     */
    @XmlEnumValue("MustUnderstand")
    MUST_UNDERSTAND,
    /**
     * 消息被不正确地构成，或包含了不正确的信息
     */
    @XmlEnumValue("Client")
    CLIENT,
    /**
     * 服务器有问题，因此无法处理进行下去
     */
    @XmlEnumValue("Server")
    SERVER
}
