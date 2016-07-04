package com.googlecode.easyec.spirit.web.controller.databind;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import com.googlecode.easyec.spirit.web.controller.formbean.annotations.Form;
import com.googlecode.easyec.spirit.web.controller.formbean.enums.SearchTermType;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.tags.SearchTermTag;
import com.googlecode.easyec.spirit.web.controller.interceptors.RequestUriReusingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import static com.googlecode.easyec.spirit.web.controller.EcController.PARAMETER_PAGE_NUMBER;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * 自定义搜索表单的参数映射解析类
 *
 * @author JunJie
 */
public class SearchFormWebArgumentResolver implements WebArgumentResolver, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SearchFormWebArgumentResolver.class);
    private static final String SEARCH_PREFIX = SearchTermTag.SEARCH_TERM_NAME_PREFIX;
    public static final String REQUEST_FORM_BEAN_PATH_SUFFIX = "/$SEARCH$";

    private String datePattern;
    private FormBeansFactory formBeansFactory;

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public void setFormBeansFactory(FormBeansFactory formBeansFactory) {
        this.formBeansFactory = formBeansFactory;
    }

    public void afterPropertiesSet() throws Exception {
        if (formBeansFactory == null) {
            throw new IllegalArgumentException("FormBeansFactory is null.");
        }

        if (StringUtils.isBlank(datePattern)) {
            datePattern = "yyyy-MM-dd";
        }
    }

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        Class<?> parameterType = methodParameter.getParameterType();
        if (!AbstractSearchFormBean.class.isAssignableFrom(parameterType)) {
            logger.debug("Parameter type: [" + parameterType + "] isn't assignable from FormBean.class");

            return UNRESOLVED;
        }

        Form fa = methodParameter.getParameterAnnotation(Form.class);
        if (fa == null) {
            logger.debug("FormBean.class hasn't assigned with annotation @Form.class");

            return UNRESOLVED;
        }

        String requestUri = (String) webRequest.getAttribute(RequestUriReusingInterceptor.THIS_REQUEST_URI, SCOPE_REQUEST);

        if (StringUtils.isBlank(requestUri)) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            requestUri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        }

        logger.debug("Request URI: [" + requestUri + "].");

        String name = requestUri + REQUEST_FORM_BEAN_PATH_SUFFIX;
        logger.debug("Current form bean's path: [" + name + "].");

        FormBean formBean = formBeansFactory.findFormBean(webRequest.getSessionId(), name);
        if (formBean == null) {
            formBean = (AbstractSearchFormBean) BeanUtils.instantiateClass(parameterType);

            ((AbstractSearchFormBean) formBean).setId(fa.value());
            ((AbstractSearchFormBean) formBean).setUri(name);

            formBeansFactory.addFormBean(webRequest.getSessionId(), formBean);
        } else {
            if (!(formBean instanceof AbstractSearchFormBean)) {
                String msg = "Object in session with key: [" + name + "], wasn't "
                        + AbstractSearchFormBean.class.getName() + ". So it's a bad object type.";

                logger.error(msg);

                throw new IllegalArgumentException(msg);
            }
        }

        logger.debug("do copy property which are in AbstractSearchFormBean.class");
        copy((AbstractSearchFormBean) formBean, webRequest);

        logger.debug("push FormBean to object WebRequest that scope is in request.");
        webRequest.setAttribute(fa.value(), formBean, SCOPE_REQUEST);

        return formBean;
    }

    protected void copy(AbstractSearchFormBean formBean, WebRequest webRequest) {
        Iterator<String> parameterNames = webRequest.getParameterNames();
        while (parameterNames.hasNext()) {
            String parameterName = parameterNames.next();

            if (parameterName.startsWith(SEARCH_PREFIX)) {
                String parts[] = parameterName.replaceAll(SEARCH_PREFIX, "").split(":");

                String key = null;
                SearchTermType type = SearchTermType.UNKNOWN;
                Class enumClass = null;

                if (parts.length == 1) {
                    key = parts[0];
                } else if (parts.length == 2) {
                    key = parts[0];
                    type = SearchTermType.valueOf(parts[1]);
                } else if (parts.length == 3) {
                    key = parts[0];
                    type = SearchTermType.valueOf(parts[1]);
                    enumClass = ClassUtils.resolveClassName(parts[2], ClassUtils.getDefaultClassLoader());
                }

                String v = webRequest.getParameter(parameterName);
                if (StringUtils.isNotBlank(v)) {
                    Object o = cast(v, type, enumClass);

                    if (o != null) {
                        formBean.addSearchTerm(key, o);
                    }
                } else {
                    formBean.removeSearchTerm(key);
                }

                continue;
            }

            if (PARAMETER_PAGE_NUMBER.equals(parameterName)) {
                String pageNumber = webRequest.getParameter(PARAMETER_PAGE_NUMBER);
                if (StringUtils.isBlank(pageNumber)) {
                    pageNumber = "1";
                }

                formBean.setPageNumber(Integer.parseInt(pageNumber));
            }
        }
    }

    private Object cast(String value, SearchTermType type, Class enumType) {
        switch (type) {
            case STRING:
                return value.trim();
            case DATE:
                try {
                    return new SimpleDateFormat(datePattern).parse(value);
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);

                    return value;
                }
            case DOUBLE:
                return Double.valueOf(value);
            case INTEGER:
                return Integer.valueOf(value);
            case ENUM:
                return Enum.valueOf(enumType, value);
        }

        return value;
    }
}
