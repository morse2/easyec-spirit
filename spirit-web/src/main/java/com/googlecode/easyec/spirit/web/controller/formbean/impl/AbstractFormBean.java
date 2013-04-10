package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.fasterxml.uuid.Generators;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-4-24
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFormBean implements FormBean {

    private static final long serialVersionUID = 4655186881729512625L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private String id;
    private String uri;
    private String token;
    private FormMethod method;

    protected AbstractFormBean() {
        createToken();
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public FormMethod getFormMethod() {
        return method;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(FormMethod method) {
        this.method = method;
    }

    protected void createToken() {
        this.token = Generators.randomBasedGenerator().generate().toString();
    }
}
