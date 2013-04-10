package com.googlecode.easyec.spirit.web.soap;

import org.dom4j.Namespace;

/**
 * XML形式的SOAP消息类。
 *
 * @author JunJie
 */
public interface Soap extends Envelope {

    /**
     * 得到此SOAP协议的命名空间。
     *
     * @return {@link Namespace}
     */
    Namespace getNamespace();
}
