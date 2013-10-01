package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.zkoss.builder.paratemters.FormParameter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.easyec.zkoss.mvvm.BaseFormVM.ARG_FORM_OBJECT;

/**
 * @author JunJie
 */
public class FormBuilder implements InitializingBean {

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

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(componentBuilder, "ComponentBuilder object cannot be null.");
    }
}
