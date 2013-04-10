package com.googlecode.easyec.spirit.web.controller.formbean.tags;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-4-27
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public class SearchFormBeanTag extends FormBeanTagSupport {

    private static final long serialVersionUID = 5957886470200398213L;

    @Override
    protected FormBean createFormBean() {
        return new SearchFormBean();
    }

    @Override
    protected void printBeginFormAsHtml() throws JspException {
        super.printBeginFormAsHtml();

        StringBuffer sb = new StringBuffer();
        sb.append("<input id=\"pageNumber\" name=\"pageNumber\" type=\"hidden\" value=\"");
        sb.append(((SearchFormBean) getFormBean()).getPageNumber()).append("\">");

        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
