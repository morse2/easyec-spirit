package com.googlecode.easyec.spirit.web.soap.factory;

import com.googlecode.easyec.spirit.web.soap.impl.AbstractSoap;
import org.dom4j.Namespace;

/**
 * 内部默认的SOAP对象封装类。
 *
 * @author JunJie
 */
final class DefaultSoap extends AbstractSoap {

    private Namespace namespace;

    public DefaultSoap(Object header, Object body, Object fault, Namespace namespace) {
        super(header, body, fault);
        this.namespace = namespace;
    }

    public Namespace getNamespace() {
        return namespace;
    }
}
