package com.googlecode.easyec.spirit.web.controller.formbean.tags;

import com.googlecode.easyec.spirit.web.controller.formbean.enums.SearchTermType;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import java.util.Date;
import java.util.Map;

import static javax.servlet.jsp.PageContext.PAGE_SCOPE;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-4-27
 * Time: 下午8:44
 * To change this template use File | Settings | File Templates.
 */
public class SearchTermTag extends BodyTagSupport {

    public static final String SEARCH_TERM_NAME_PREFIX = "s_";
    private static final String FIELD_SEARCH_TERM_NAME = "searchTermName";
    private static final String FIELD_SEARCH_TERM_VALUE = "searchTermValue";
    private static final long serialVersionUID = -3849635084193617923L;

    private String prefix = SEARCH_TERM_NAME_PREFIX;

    private String name;
    private String javaType;
    private String enumType;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    @Override
    public int doStartTag() throws JspException {
        if (StringUtils.isBlank(name)) {
            return SKIP_BODY;
        }

        StringBuffer searchName = new StringBuffer();
        searchName.append(SEARCH_TERM_NAME_PREFIX).append(name);

        Map<String, Object> map = findSearchFormBean().getSearchTerms();
        if (!map.isEmpty() && map.containsKey(name)) {
            Object value = map.get(name);

            SearchTermType termType;
            if (value instanceof String) {
                termType = SearchTermType.STRING;
            } else if (value instanceof Integer) {
                termType = SearchTermType.INTEGER;
            } else if (value instanceof Double) {
                termType = SearchTermType.DOUBLE;
            } else if (value instanceof Date) {
                termType = SearchTermType.DATE;
            } else {
                termType = SearchTermType.UNKNOWN;
            }

            searchName.append(":").append(termType.name());
            pageContext.setAttribute(FIELD_SEARCH_TERM_VALUE, value, PAGE_SCOPE);
        } else {
            boolean isEnumType = false;
            SearchTermType termType = SearchTermType.STRING;
            if (StringUtils.isNotBlank(javaType)) {
                try {
                    termType = SearchTermType.valueOf(javaType);

                    if (SearchTermType.ENUM.equals(termType)) {
                        if (StringUtils.isBlank(enumType)) {
                            throw new JspException("enumType mustn't be null when search term type is ENUM.");
                        }

                        isEnumType = true;
                    }
                } catch (IllegalArgumentException e) {
                    termType = SearchTermType.UNKNOWN;
                }
            }

            searchName.append(":").append(termType.name());
            if (isEnumType) {
                searchName.append(":").append(enumType);
            }
        }

        pageContext.setAttribute(FIELD_SEARCH_TERM_NAME, searchName.toString(), PAGE_SCOPE);

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(FIELD_SEARCH_TERM_NAME, PAGE_SCOPE);
        pageContext.removeAttribute(FIELD_SEARCH_TERM_VALUE, PAGE_SCOPE);

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        name = javaType = enumType = null;
        prefix = SEARCH_TERM_NAME_PREFIX;
    }

    protected AbstractSearchFormBean findSearchFormBean() throws JspTagException {
        Tag tag = getParent();

        do {
            if (tag == null) {
                break;
            }

            if (tag instanceof SearchFormBeanTag) {
                return (AbstractSearchFormBean) ((SearchFormBeanTag) tag).getFormBean();
            }

            tag = tag.getParent();
        } while (true);

        throw new JspTagException("Illegal use of &lt;searchTerm&gt;-style tag without " +
                "&lt;formBean&gt; as its tag");
    }
}
