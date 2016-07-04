package com.googlecode.easyec.spirit.web.controller.formbean.tags;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBeanParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import static javax.servlet.jsp.PageContext.REQUEST_SCOPE;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-4-27
 * Time: 下午11:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class FormBeanTagSupport extends TagSupport implements FormBeanParam {

    private static final long serialVersionUID = 7268613272422943675L;
    private FormBean formBean;
    protected String method = "POST";
    protected String uri;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int doStartTag() throws JspException {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id mustn't be null.");
        }

        Object v = pageContext.getAttribute(id, REQUEST_SCOPE);
        if (v == null) {
            formBean = createFormBean();
            pageContext.setAttribute(id, REQUEST_SCOPE);
        } else {
            formBean = (FormBean) v;
        }

        initFormBean();
        printBeginFormAsHtml();

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        printEndFormAsHtml();

        return EVAL_PAGE;
    }

    /**
     * 创建一个具体的表单BEAN对象
     *
     * @return {@link FormBean}
     * @see com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean
     * @see com.googlecode.easyec.spirit.web.controller.formbean.impl.CreationFormBean
     * @see com.googlecode.easyec.spirit.web.controller.formbean.impl.ModificationFormBean
     */
    abstract protected FormBean createFormBean();

    /**
     * 初始化表单BEAN的方法
     */
    protected void initFormBean() {
        BeanWrapperImpl bw = new BeanWrapperImpl(formBean);

        if (bw.isWritableProperty("id")) {
            bw.setPropertyValue("id", id);
        }

        if (bw.isWritableProperty("uri")) {
            bw.setPropertyValue("uri", uri);
        }

        if (bw.isWritableProperty("formMethod")) {
            if (StringUtils.isNotBlank(method)) {
                bw.setPropertyValue("formMethod", FormBean.FormMethod.valueOf(method.toUpperCase()));
            }
        }
    }

    protected void printBeginFormAsHtml() throws JspException {
        StringBuffer sb = new StringBuffer();

        sb.append("<form id=\"").append(formBean.getId()).append("\" ");
        sb.append("name=\"").append(formBean.getId()).append("\" ");
        sb.append("method=\"").append(method).append("\" ");
        sb.append(" action=\"").append(uri).append("\">");
        sb.append("<input name=\"token\" type=\"hidden\" value=\"").append(formBean.getToken()).append("\">");

        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    protected void printEndFormAsHtml() throws JspException {
        StringBuffer sb = new StringBuffer();
        sb.append("</form>");

        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    @Override
    public void release() {
        formBean = null;
        id = uri = null;
        method = "POST";
    }

    public FormBean getFormBean() {
        return formBean;
    }
}
