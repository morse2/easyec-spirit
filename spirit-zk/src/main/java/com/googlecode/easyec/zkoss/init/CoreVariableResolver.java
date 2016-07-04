package com.googlecode.easyec.zkoss.init;

import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static com.googlecode.easyec.spirit.web.controller.interceptors.RequestUriReusingInterceptor.THIS_REQUEST_URI;

/**
 * 核心变量解析类
 *
 * @author JunJie
 */
public class CoreVariableResolver implements VariableResolver, Serializable {

    private static final long serialVersionUID = -4717186963656748380L;

    @Override
    public Object resolveVariable(String name) throws XelException {
        if (Components.isImplicit(name) || "event".equals(name)) {
            return null;
        }

        if ("thisUri".equals(name)) {
            return _getNativeRequest().getAttribute(THIS_REQUEST_URI);
        }

        if ("base".equals(name)) {
            return _getNativeRequest().getContextPath();
        }

        return null;
    }

    private HttpServletRequest _getNativeRequest() {
        return (HttpServletRequest) Executions.getCurrent().getNativeRequest();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
