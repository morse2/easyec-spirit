package com.googlecode.easyec.zkspring.ui;

import com.googlecode.easyec.zkoss.utils.ExecUtils;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
import org.zkoss.zk.ui.Components;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static com.googlecode.easyec.spirit.web.controller.interceptors.RequestUriReusingInterceptor.PREV_REQUEST_URI;
import static com.googlecode.easyec.spirit.web.controller.interceptors.RequestUriReusingInterceptor.THIS_REQUEST_URI;

/**
 * @author junjie
 */
public class DefaultVariableResolver implements VariableResolver, Serializable {

    private static final long serialVersionUID = 49089026863421054L;

    @Override
    public Object resolveVariable(String name) throws XelException {
        if (Components.isImplicit(name) || "event".equals(name)) {
            return null;
        }

        HttpServletRequest request = ExecUtils.getNativeRequest();
        if (request == null) return null;

        if ("thisUri".equals(name)) {
            return request.getAttribute(THIS_REQUEST_URI);
        }

        if ("prevUri".equals(name)) {
            return request.getAttribute(PREV_REQUEST_URI);
        }

        if ("base".equals(name)) {
            return request.getContextPath();
        }

        return null;
    }
}
