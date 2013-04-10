package com.googlecode.easyec.spirit.web.soap.impl;

import com.googlecode.easyec.spirit.web.soap.Body;
import com.googlecode.easyec.spirit.web.soap.Fault;
import com.googlecode.easyec.spirit.web.soap.Header;
import com.googlecode.easyec.spirit.web.soap.Soap;

/**
 * SOAP抽象实现类。
 *
 * @author JunJie
 */
public abstract class AbstractSoap implements Soap {

    private Header header;
    private Fault fault;
    private Body body;

    public AbstractSoap(Object body) {
        this(null, body);
    }

    public AbstractSoap(Object header, Object body) {
        this(header, body, null);
    }

    public AbstractSoap(Object header, Object body, Object fault) {
        this.header = new InternalHeader(header);
        this.fault = new InternalFault(fault);
        this.body = new InternalBody(body);
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public Fault getFault() {
        return fault;
    }

    private class InternalHeader implements Header {

        private Object object;

        private InternalHeader(Object object) {
            this.object = object;
        }

        public Object getContent() {
            return object;
        }
    }

    private class InternalBody implements Body {

        private Object object;

        private InternalBody(Object object) {
            this.object = object;
        }

        public Object getContent() {
            return object;
        }
    }

    private class InternalFault implements Fault {

        private Object object;

        private InternalFault(Object object) {
            this.object = object;
        }

        public Object getContent() {
            return object;
        }
    }
}
