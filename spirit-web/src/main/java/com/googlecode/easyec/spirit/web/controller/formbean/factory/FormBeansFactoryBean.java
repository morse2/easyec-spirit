package com.googlecode.easyec.spirit.web.controller.formbean.factory;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-2
 * Time: 上午12:17
 * To change this template use File | Settings | File Templates.
 */
public class FormBeansFactoryBean implements FactoryBean<FormBeansFactory>, InitializingBean {

    private FormBeansFactory formBeansFactory;

    public void setFormBeansFactory(FormBeansFactory formBeansFactory) {
        this.formBeansFactory = formBeansFactory;
    }

    public FormBeansFactory getObject() throws Exception {
        return formBeansFactory;
    }

    public Class<?> getObjectType() {
        return FormBeansFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (formBeansFactory == null) {
            formBeansFactory = new DefaultFormBeansFactory();
        }
    }
}
