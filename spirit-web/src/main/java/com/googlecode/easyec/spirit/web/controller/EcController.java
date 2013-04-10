package com.googlecode.easyec.spirit.web.controller;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.util.UriComponents;
import com.googlecode.easyec.spirit.web.controller.util.UriComponentsBuilder;
import com.googlecode.easyec.spirit.web.message.MessageUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.googlecode.easyec.spirit.web.controller.databind.SearchFormWebArgumentResolver.REQUEST_FORM_BEAN_PATH_SUFFIX;
import static com.googlecode.easyec.spirit.web.controller.interceptors.RequestUriReusingInterceptor.THIS_REQUEST_URI;

/**
 * Easy Commerce Controller class.
 *
 * @author JunJie
 */
public abstract class EcController {

    public static final String PARAMETER_PAGE_NUMBER = "pageNumber";

    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private MessageSource messageSource;
    private FormBeansFactory formBeansFactory;

    @Autowired(required = false)
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired(required = false)
    public void setFormBeansFactory(FormBeansFactory formBeansFactory) {
        this.formBeansFactory = formBeansFactory;
    }

    /**
     * 通过给定的code，查找国际化文件中的内容。
     *
     * @param code   国际化文件中的key
     * @param locale 本地化语言
     * @return 国际化内容
     */
    protected String getMessage(String code, Locale locale) {
        return getMessage(code, locale, new Object[0]);
    }

    /**
     * 通过给定的code，查找国际化文件中的内容。
     *
     * @param code   国际化文件中的key
     * @param locale 本地化语言
     * @param params 参数
     * @return 国际化内容
     */
    protected String getMessage(String code, Locale locale, Object... params) {
        return messageSource.getMessage(code, params, locale);
    }

    /**
     * 重定向到给定的URI地址。
     *
     * @param uri 相对的URI地址
     * @return
     */
    protected String redirectTo(String uri) {
        return new StringBuffer("redirect:").append(uri).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param uri
     * @param params
     * @return
     */
    protected String redirectTo(String uri, String... params) {
        return new StringBuffer("redirect:").append(buildPath(uri, null, params).encode().toUriString()).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param uri
     * @param map
     * @return
     */
    protected String redirectTo(String uri, Map<String, Object> map) {
        return new StringBuffer("redirect:").append(buildPath(uri, map).encode().toUriString()).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param uri
     * @param map
     * @param params
     * @return
     */
    protected String redirectTo(String uri, Map<String, Object> map, String... params) {
        return new StringBuffer("redirect:").append(buildPath(uri, map, params).encode().toUriString()).toString();
    }

    /**
     * 跳转到给定的URI地址
     *
     * @param uri 相对的URI地址
     * @return
     */
    protected String forwardTo(String uri) {
        return new StringBuffer("forward:").append(uri).toString();
    }

    /**
     * 跳转到给定的URI地址。
     * 此URI地址包含了path变量。
     * 通过给定的参数来解析path变量。
     *
     * @param uri    相对的URI地址
     * @param params path路径变量对应的值
     * @return
     */
    protected String forwardTo(String uri, String... params) {
        return new StringBuffer("forward:").append(buildPath(uri, null, params).encode().toUriString()).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param uri
     * @param map
     * @return
     */
    protected String forwardTo(String uri, Map<String, Object> map) {
        return new StringBuffer("forward:").append(buildPath(uri, map).encode().toUriString()).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param uri
     * @param map
     * @param params
     * @return
     */
    protected String forwardTo(String uri, Map<String, Object> map, String... params) {
        return new StringBuffer("forward:").append(buildPath(uri, map, params).encode().toUriString()).toString();
    }

    /**
     * DOCUMENT IT
     *
     * @param path
     * @param map
     * @param params
     * @return
     */
    protected UriComponents buildPath(String path, Map<String, Object> map, String... params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);

        if (map != null) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                builder.queryParam(key, map.get(key));
            }
        }

        if (params != null && params.length > 0) {
            builder.pathSegment(params);
        }

        return builder.build();
    }

    /**
     * 返回当前列表页码
     *
     * @param request <code>HttpServletRequest</code> 对象
     * @return 页码值，默认为1
     */
    protected int getCurrentPage(HttpServletRequest request) {
        return getCurrentPage(request, PARAMETER_PAGE_NUMBER);
    }

    /**
     * 由给定的参数名，返回当前列表页码
     *
     * @param request  <code>HttpServletRequest</code> 对象
     * @param pageName 页码参数名
     * @return 页码值，默认为1
     */
    protected int getCurrentPage(HttpServletRequest request, String pageName) {
        Integer currentPage = 1;
        String parameter = request.getParameter(pageName);
        if (StringUtils.isNotBlank(parameter)) {
            currentPage = NumberUtils.parseNumber(parameter, Integer.class);
        }

        return currentPage;
    }

    /**
     * 保存一条消息至当前会话中
     *
     * @param request <code>HttpServletRequest</code> 对象
     * @param code    国际化中的code
     */
    protected void saveMessage(HttpServletRequest request, String code) {
        MessageUtils.saveMessage(request, code);
    }

    /**
     * 保存一条错误消息至当前会话中
     *
     * @param request   <code>HttpServletRequest</code> 对象
     * @param errorCode 国际化中的code
     */
    protected void saveError(HttpServletRequest request, String errorCode) {
        MessageUtils.saveError(request, errorCode);
    }

    /**
     * 获得当前请求的URL
     *
     * @param request <code>HttpServletRequest</code> 对象
     * @return 当前HTTP请求的URI
     */
    protected String getThisUri(HttpServletRequest request) {
        return (String) request.getAttribute(THIS_REQUEST_URI);
    }

    /**
     * 查找当前会话中，指定URI路径下的<code>AbstractSearchFormBean</code>对象。
     *
     * @param request <code>HttpServletRequest</code> 对象
     * @param path    URI的路径
     * @return <code>AbstractSearchFormBean</code>
     */
    protected AbstractSearchFormBean findSearchFormBean(HttpServletRequest request, String path) {
        String id = request.getSession().getId();
        logger.debug("Session id: [" + id + "], path: [" + path + "].");

        if (formBeansFactory != null) {
            return (AbstractSearchFormBean) formBeansFactory.findFormBean(id, path + REQUEST_FORM_BEAN_PATH_SUFFIX);
        }

        return null;
    }

    /**
     * 查找<code>AbstractSearchFormBean</code>对象中的搜索条件的值。
     *
     * @param request <code>HttpServletRequest</code> 对象
     * @param path    URI的路径
     * @param key     键
     * @return key对应的value
     */
    protected Object findTermFromSearchFormBean(HttpServletRequest request, String path, String key) {
        AbstractSearchFormBean formBean = findSearchFormBean(request, path);

        if (formBean != null) {
            Object o = formBean.getSearchTerms().get(key);
            logger.debug("Property in search form bean, key: [" + key + "], value: [" + o + "].");

            return o;
        }

        return null;
    }
}
