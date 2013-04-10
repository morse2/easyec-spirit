package com.googlecode.easyec.spirit.web.soap.impl;

import org.dom4j.Namespace;

import static com.googlecode.easyec.spirit.web.soap.factory.SoapFactory.SOAP_NAMESPACE_12;

/**
 * SOAP 1.2协议的实现类。
 *
 * @author JunJie
 */
public class Soap12 extends AbstractSoap {

    public Soap12(Object body) {
        super(body);
    }

    public Soap12(Object header, Object body) {
        super(header, body);
    }

    public Soap12(Object header, Object body, Object fault) {
        super(header, body, fault);
    }

    public Namespace getNamespace() {
        return new Namespace("soap12", SOAP_NAMESPACE_12);
    }
}
