package com.googlecode.easyec.zkoss;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Classes;
import org.zkoss.lang.Library;
import org.zkoss.lang.Objects;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ZUL变量解析器的委托代理类
 *
 * @author JunJie
 */
public class DelegatingVariableResolver implements VariableResolver, Serializable {

    public static final String RESOLVER_CLASS = "com.googlecode.easyec.zk.VariableResolver.class";

    private static final Logger logger = LoggerFactory.getLogger(DelegatingVariableResolver.class);
    private static final long serialVersionUID = 6294959821662123447L;
    private List<VariableResolver> _variableResolvers = new ArrayList<>();

    public DelegatingVariableResolver() {
        final String clsStr = Library.getProperty(RESOLVER_CLASS);
        String[] classes = StringUtils.split(clsStr, ",");
        if (ArrayUtils.isNotEmpty(classes)) {
            for (String cls : classes) {
                try {
                    VariableResolver o = (VariableResolver)
                        Classes.newInstanceByThread(cls.trim());
                    if (!_variableResolvers.contains(o)) {
                        _variableResolvers.add(o);
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public Object resolveVariable(String name) throws XelException {
        Object ret = null;

        for (VariableResolver resolver : _variableResolvers) {
            ret = resolver.resolveVariable(name);
            if (ret != null) break;
        }

        return ret;
    }

    public int hashCode() {
        return Objects.hashCode(_variableResolvers);
    }

    public boolean equals(Object obj) {
        return this == obj || (obj instanceof DelegatingVariableResolver
            && Objects.equals(_variableResolvers, ((DelegatingVariableResolver) obj)._variableResolvers));
    }

    // -- Serializable --//
    private synchronized void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(_variableResolvers.size());
        for (VariableResolver resolver : _variableResolvers) {
            s.writeObject(resolver);
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int size = s.readInt();
        for (int i = 0; i < size; i++) {
            _variableResolvers.add((VariableResolver) s.readObject());
        }
    }
}
