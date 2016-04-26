package com.googlecode.easyec.zkoss;

import org.zkoss.lang.Classes;
import org.zkoss.lang.Library;
import org.zkoss.lang.Objects;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ZUL变量解析器的委托代理类
 *
 * @author JunJie
 */
public class DelegatingVariableResolver implements VariableResolver, Serializable {

    public static final String RESOLVER_CLASS = "com.googlecode.easyec.zkoss.VariableResolver.class";
    private static final long serialVersionUID = -1796672500062108417L;

    private List<VariableResolver> variableResolvers = new ArrayList<VariableResolver>();

    public DelegatingVariableResolver() {
        final String classes = Library.getProperty(RESOLVER_CLASS);

        String[] vrClss = classes.split(",");
        for (String vrCls : vrClss) {
            try {
                VariableResolver o = (VariableResolver) Classes.newInstanceByThread(vrCls);
                if (!variableResolvers.contains(o)) {
                    variableResolvers.add(o);
                }
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    @Override
    public Object resolveVariable(String name) throws XelException {
        Object o = null;
        for (final Iterator it = variableResolvers.iterator(); it.hasNext(); ) {
            VariableResolver resolver = (VariableResolver) it.next();
            o = resolver.resolveVariable(name);
            if (o != null) {
                return o;
            }
        }

        return o;
    }

    public int hashCode() {
        return Objects.hashCode(variableResolvers);
    }

    public boolean equals(Object obj) {
        return this == obj || (obj instanceof DelegatingVariableResolver
            && Objects.equals(variableResolvers, ((DelegatingVariableResolver) obj).variableResolvers));
    }
}
