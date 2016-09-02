package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.zkoss.builder.paratemters.FormParameter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.easyec.zkoss.mvvm.BaseFormVM.ARG_FORM_OBJECT;
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
