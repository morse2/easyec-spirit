package com.googlecode.easyec.spirit.web.controller.sorts.tags;

import com.googlecode.easyec.spirit.web.message.MessageUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import static javax.servlet.jsp.PageContext.PAGE_SCOPE;

/**
 * @author JunJie
 */
public class MessageTag extends BodyTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(MessageTag.class);
    private static final long serialVersionUID = -1020114504351385135L;
    private String var;
    private boolean error;

    public void setVar(String var) {
        this.var = var;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public void release() {
        super.release();
        var = null;
    }

    @Override
    public int doStartTag() throws JspException {
        String message;

        if (error) {
            message = MessageUtils.getError((HttpServletRequest) pageContext.getRequest());
        } else {
            message = MessageUtils.getMessage((HttpServletRequest) pageContext.getRequest());
        }

        if (StringUtils.isBlank(message)) {
            return SKIP_BODY;
        }

        pageContext.setAttribute(var, message, PAGE_SCOPE);
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(var, PAGE_SCOPE);

        return EVAL_PAGE;
    }
}
