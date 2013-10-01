package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.zkoss.builder.paratemters.ComponentParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.zkoss.zk.ui.Component;

import java.util.Map;

import static com.googlecode.easyec.zkoss.builder.paratemters.ComponentParameter.ComponentParameterBuilder;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.zkoss.zk.ui.Executions.createComponents;

/**
 * ZK组件页面构建器类。
 *
 * @author JunJie
 */
public final class ComponentBuilder implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ComponentBuilder.class);
    private String prefix;
    private String suffix;
    private final StringBuffer pathPattern = new StringBuffer();

    /**
     * 设置页面路径的前缀。
     *
     * @param prefix 路径前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 设置页面路径的后缀。
     *
     * @param suffix 页面后缀
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 创建组件。
     *
     * @param uri 组件相对路径名
     * @return ZK组件对象
     */
    public <T extends Component> T create(String uri) {
        return create(uri, null, null);
    }

    /**
     * 创建组件。
     *
     * @param uri    组件相对路径名
     * @param parent 当前被创建组件的父组件
     * @return ZK组件对象
     */
    public <T extends Component> T create(String uri, Component parent) {
        return create(uri, parent, null);
    }

    /**
     * 创建组件。
     *
     * @param uri      组件相对路径名
     * @param paramMap 当前组件接受的参数
     * @return ZK组件对象
     */
    public <T extends Component> T create(String uri, Map<String, Object> paramMap) {
        return create(uri, null, paramMap);
    }

    /**
     * 创建组件。
     *
     * @param uri      组件相对路径名
     * @param parent   当前被创建组件的父组件
     * @param paramMap 当前组件接受的参数
     * @return ZK组件对象
     */
    public <T extends Component> T create(String uri, Component parent, Map<String, Object> paramMap) {
        return create(
            new ComponentParameterBuilder(uri)
                .parameters(paramMap)
                .parent(parent)
                .build()
        );
    }

    public <T extends Component> T create(ComponentParameter parameter) {
        return doCreate(parameter);
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> T doCreate(ComponentParameter parameter) {
        if (isBlank(parameter.getUri())) return null;

        String path = pathPattern.toString();
        String page = parameter.getUri();
        if (page.startsWith("/")) {
            page = page.substring(1);
        }

        path = path.replaceAll("\\{Placeholder}", page);
        logger.debug("Path to create component: [" + path + "].");

        return (T) createComponents(
            path,
            parameter.getParent(),
            parameter.getParameters()
        );
    }

    public void afterPropertiesSet() throws Exception {
        if (isNotBlank(prefix)) pathPattern.append(prefix);
        pathPattern.append("{Placeholder}");
        if (isNotBlank(suffix)) pathPattern.append(suffix);
    }
}
