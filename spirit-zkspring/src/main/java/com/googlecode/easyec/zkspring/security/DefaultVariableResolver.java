package com.googlecode.easyec.zkspring.security;

import com.googlecode.easyec.zkoss.utils.ExecUtils;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
import org.zkoss.zk.ui.Components;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author junjie
 */
public class DefaultVariableResolver implements VariableResolver, Serializable {

    private static final long serialVersionUID = 1327052244980212790L;

    @Override
    public Object resolveVariable(String name) throws XelException {
        if (Components.isImplicit(name) || "event".equals(name)) {
            return null;
        }

        HttpServletRequest request = ExecUtils.getNativeRequest();
        if (request == null) return null;

        if ("authentication".equals(name)) {
            return request.getUserPrincipal();
        }

        return null;
    }
}
