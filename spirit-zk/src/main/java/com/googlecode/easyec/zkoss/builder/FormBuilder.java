package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.zkoss.builder.paratemters.FormParameter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.easyec.zkoss.mvvm.BaseFormVM.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author JunJie
 */
public class FormBuilder implements InitializingBean {

    /**
     * 返回内容是JS的键信息
     */
    public static final String JS_SCRIPT = "com.googlecode.easyec.zkoss.form.result.JS";
    private ComponentBuilder componentBuilder;

    /**
     * 设置ZK组件构建器的实例对象
     *
     * @param componentBuilder ZK组件构建对象
     */
    public void setComponentBuilder(ComponentBuilder componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    /**
     * 创建ZK表单组件
     *
     * @param parameter 表单参数
     * @param <T>       返回组件的泛型类型
     * @return ZK组件实例对象
     */
    public <T extends Component> T create(FormParameter parameter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ARG_FORM_OBJECT, parameter.getObject());
        parameters.putAll(parameter.getParameters());

        return componentBuilder.create(
            parameter.getUri(),
            parameter.getParent(),
            parameters
        );
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri 页面的URI
     * @param dm  表单对象
     */
    public void redirectTo(String uri, GenericPersistentDomainModel<?> dm) {
        redirectTo(uri, dm, true);
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri   页面的URI
     * @param dm    表单对象
     * @param vmCls 匹配VM的类型
     */
    public void redirectTo(String uri, GenericPersistentDomainModel<?> dm, Class<?> vmCls) {
        redirectTo(uri, null, dm, true, vmCls);
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri 页面的URI
     * @param dm  表单对象
     * @param b   标识是否对DomainModel对象主键进行为空的判断
     */
    public void redirectTo(String uri, GenericPersistentDomainModel<?> dm, boolean b) {
        redirectTo(uri, null, dm, b);
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri    页面的URI
     * @param target 目标浏览器。例如_self、_blank
     * @param dm     表单对象
     */
    public void redirectTo(String uri, String target, GenericPersistentDomainModel<?> dm) {
        redirectTo(uri, target, dm, true);
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri    页面的URI
     * @param target 目标浏览器。例如_self、_blank
     * @param dm     表单对象
     * @param b      标识是否对DomainModel对象主键进行为空的判断
     */
    public void redirectTo(String uri, String target, GenericPersistentDomainModel<?> dm, boolean b) {
        redirectTo(uri, target, dm, b, null);
    }

    /**
     * 重定向页面到给定的URI
     *
     * @param uri    页面的URI
     * @param target 目标浏览器。例如_self、_blank
     * @param dm     表单对象
     * @param b      标识是否对DomainModel对象主键进行为空的判断
     * @param vmCls  匹配VM的类型
     */
    public void redirectTo(String uri, String target, GenericPersistentDomainModel<?> dm, boolean b, Class<?> vmCls) {
        Execution current = Executions.getCurrent();
        if (null != dm) {
            current.getSession().setAttribute(ARG_CHECK_UIDPK, b);
            current.getSession().setAttribute(ARG_FORM_OBJECT, dm);
            current.getSession().setAttribute(ARG_REQUEST_URI, uri);
            current.getSession().setAttribute(ARG_MATCH_VM, vmCls);
        }

        current.sendRedirect(uri, target);
    }

    /**
     * 重定向页面到给定URI
     *
     * @param uri 页面的URI
     * @param js  可以执行的js
     */
    public void redirectTo(String uri, String js) {
        Execution current = Executions.getCurrent();
        if (isNotBlank(js)) {
            current.getSession().setAttribute(JS_SCRIPT, js);
        }

        current.sendRedirect(uri);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(componentBuilder, "ComponentBuilder object cannot be null.");
    }
}
