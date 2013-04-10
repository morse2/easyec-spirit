package com.googlecode.easyec.spirit.web.controller.formbean;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-2
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
public interface FormBeansFactory {

    boolean addFormBean(String sessionId, FormBean bean);

    FormBean findFormBean(String sessionId, String path);

    Map<String, FormBean> find(String sessionId);

    boolean removeAll(String sessionId);

    boolean removeFormBean(String sessionId, String path);
}
