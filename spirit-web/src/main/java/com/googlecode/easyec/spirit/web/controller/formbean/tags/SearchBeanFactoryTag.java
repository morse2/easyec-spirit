package com.googlecode.easyec.spirit.web.controller.formbean.tags;

import com.googlecode.easyec.spirit.web.controller.databind.SearchFormWebArgumentResolver;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspException;

import static javax.servlet.jsp.PageContext.PAGE_SCOPE;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-6-19
 * Time: 下午11:40
 * To change this template use File | Settings | File Templates.
 */
public class SearchBeanFactoryTag extends FormBeansFactoryTagSupport {

    private static final long serialVersionUID = 2491358698966395606L;

    public void setVar(String var) {
        this.var = var;
    }

    public void setPath(String path) {
        this.path = path + SearchFormWebArgumentResolver.REQUEST_FORM_BEAN_PATH_SUFFIX;
    }

    @Override
    public int doStartTag() throws JspException {
        FormBean formBean = getFormBean();
        if (formBean != null && StringUtils.hasText(var)) {
            pageContext.setAttribute(var, formBean, PAGE_SCOPE);
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
