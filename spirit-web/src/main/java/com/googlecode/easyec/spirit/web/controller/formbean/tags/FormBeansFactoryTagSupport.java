package com.googlecode.easyec.spirit.web.controller.formbean.tags;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import com.googlecode.easyec.spirit.web.tags.EcTagSupport;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import static javax.servlet.jsp.PageContext.PAGE_SCOPE;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-6-19
 * Time: 下午11:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class FormBeansFactoryTagSupport extends EcTagSupport {

    private static final long serialVersionUID = -6201045210326194839L;
    protected String var;
    protected String path;

    @Override
    public int doStartTag() throws JspException {
        FormBean formBean = getFormBean();
        if (formBean != null && StringUtils.hasText(var)) {
            pageContext.setAttribute(var, formBean, PAGE_SCOPE);
            return EVAL_BODY_INCLUDE;
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(var, PAGE_SCOPE);
        return EVAL_PAGE;
    }

    protected FormBean getFormBean() {
        String sessionId = ((HttpServletRequest) pageContext.getRequest()).getSession().getId();
        logger.debug("Current session id of user, [" + sessionId + "].");

        return getBean(FormBeansFactory.class).findFormBean(sessionId, path);
    }

    @Override
    public void release() {
        var = path = null;
    }
}
