package com.googlecode.easyec.spirit.web.soap.impl;

import org.dom4j.Namespace;

import static com.googlecode.easyec.spirit.web.soap.factory.SoapFactory.SOAP_NAMESPACE_11;

/**
 * SOAP 1.1协议的实现类。
 *
 * @author JunJie
 */
public class Soap11 extends AbstractSoap {

    public Soap11(Object body) {
        super(body);
    }

    public Soap11(Object header, Object body) {
        super(header, body);
    }

    public Soap11(Object header, Object body, Object fault) {
        super(header, body, fault);
    }

    public Namespace getNamespace() {
        return new Namespace("soap", SOAP_NAMESPACE_11);
    }
}
