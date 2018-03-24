package com.googlecode.easyec.zkex.zul;

import org.zkoss.util.Maps;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.ext.DynamicPropertied;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.ListModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CollectionTemplate implements DynamicPropertied, Serializable {

    private static final long serialVersionUID = 4330607241054248198L;
    private boolean _autodrop;
    private Component _host;
    private ForEachEx _forEach;
    private Map<String, Object> _props;
    private String _template;
    private String _templateURI;
    private ExecutionTemplate _execTemplate;

    public CollectionTemplate(boolean autodrop) {
        this._autodrop = autodrop;
        init();
    }

    private void init() {
        this._props = new LinkedHashMap<>();
        this._forEach = new ForEachEx();
        this._execTemplate = new ExecutionTemplate();
    }

    public Component getShadowHost() {
        return this._host;
    }

    public void setModel(ListModel model) {
        this._forEach.setItems(model);
    }

    public ListModel getModel() {
        return (ListModel) this._forEach.getItems();
    }

    public void setTemplateResolver(CollectionTemplateResolver templateResolver) {
        this._execTemplate.setTemplateResolver(templateResolver);
    }

    public String getTemplate() {
        return this._template;
    }

    public void setTemplate(String template) {
        this._template = template;
        this._execTemplate.setTemplateResolver(new CollectionTemplateResolverImpl(template));
    }

    public void setTemplateURI(String templateURI) {
        this._templateURI = templateURI;
        this._execTemplate.setTemplateResolver(new CollectionTemplateResolverImpl(templateURI, this._props));
    }

    public String getTemplateURI() {
        return this._templateURI;
    }

    public void apply(Component host) {
        if (this._autodrop) {
            applyDropTrue(host);
        } else {
            applyDropFalse(host);
        }
    }

    private void applyDropTrue(Component host) {
        if (host == null) {
            Component firstInsertion = this._forEach.getFirstInsertion();
            if (firstInsertion != null) {
                Component lastInsertion = this._forEach.getLastInsertion();
                Component next = firstInsertion;
                for (Component end = lastInsertion.getNextSibling(); next != end; ) {
                    Component tmp = next.getNextSibling();
                    next.detach();
                    next = tmp;
                }
            }

            this._forEach.mergeSubTree();
            this._forEach.detach();
            this._host = null;

            return;
        }

        if (this._host != null) {
            if (this._host != host) {
                throw new UiException("The shadow element cannot change its host, if existed. ["
                    + this + "], please apply with null first!.");
            }
        } else {
            this._host = host;
            this._forEach.setShadowHost(this._host, null);
        }

        if (this._forEach.getAfterCompose()) {
            this._forEach.recreate();
        } else {
            this._forEach.afterCompose();
        }
    }

    private void applyDropFalse(Component host) {
        if (host == null) {
            throw new UiException("The shadow host cannot be null. [" + this + "].");
        }

        this._host = host;
        this._forEach.setShadowHost(this._host, null);

        if (this._forEach.getAfterCompose()) {
            this._forEach.recreate();
        } else {
            this._forEach.afterCompose();
        }

        this._forEach.mergeSubTree();
        this._forEach.detach();
    }

    public boolean hasDynamicProperty(String name) {
        return this._props.containsKey(name);
    }

    public Object getDynamicProperty(String name) {
        return this._props.get(name);
    }

    public Map<String, Object> getDynamicProperties() {
        return this._props;
    }

    public void setDynamicProperty(String name, Object value) throws WrongValueException {
        this._props.put(name, value);
    }

    private class ForEachEx extends ForEach {

        private static final long serialVersionUID = 7970392257877621078L;

        public void detach() {
            if (CollectionTemplate.this._autodrop) {
                ((ListModel) getItems()).removeListDataListener(this._dataListener);
            }
            super.detach();
        }

        public Template getTemplate(String name) {
            if ((name != null) && (!name.isEmpty())) return super.getTemplate(name);
            return CollectionTemplate.this._execTemplate;
        }

        public Object resolveVariable(Component child, String name, boolean recurse) {
            Object result = CollectionTemplate.this._props.get(name);
            if (result == null) {
                return super.resolveVariable(child, name, recurse);
            }
            return result;
        }

        public void mergeSubTree() {
            super.mergeSubTree();
        }

        private boolean getAfterCompose() {
            return this._afterComposed;
        }
    }

    private class ExecutionTemplate implements Template {

        CollectionTemplateResolver<Object> _templateResolver;

        private ExecutionTemplate() {}

        private ExecutionTemplate(CollectionTemplateResolver<Object> templateResolver) {
            this._templateResolver = templateResolver;
        }

        private void setTemplateResolver(CollectionTemplateResolver<Object> templateResolver) {
            this._templateResolver = templateResolver;
        }

        public CollectionTemplateResolver<Object> getTemplateResolver() {
            return this._templateResolver;
        }

        public Component[] create(Component parent, Component insertBefore, VariableResolver resolver, Composer composer) {
            Template resolvedTemplate = this._templateResolver.resolve(
                resolver.resolveVariable(CollectionTemplate.this._forEach.getVar())
            );

            if (null != resolvedTemplate) {
                return resolvedTemplate.create(parent, insertBefore, resolver, composer);
            }

            return null;
        }

        public Map<String, Object> getParameters() {
            return null;
        }
    }


    private class CollectionTemplateResolverImpl implements CollectionTemplateResolver<Object> {

        private String _templateName;
        private String _uri;
        private Map _arg;
        private Map<String, Template> _templateCache = new HashMap<>(4);

        private CollectionTemplateResolverImpl(String templateName) {
            this._templateName = templateName;
        }

        private CollectionTemplateResolverImpl(String uri, Map<?, ?> arg) {
            this._uri = uri;
            this._arg = arg;
        }

        private Template lookupTemplate(Component comp, ForEachEx base, String name) {
            Template templateFromCache = this._templateCache.get(name);
            if (null != templateFromCache) return templateFromCache;

            Template _t = Templates.lookup(comp, base, name, null, true);
            if (null != _t) this._templateCache.put(name, _t);

            return _t;
        }

        private Template getTemplateURI() {
            if (isNotBlank(this._uri)) {
                String uri = this._uri;
                int queryStart = uri.indexOf('?');
                String queryString = null;
                if (queryStart >= 0) {
                    queryString = uri.substring(queryStart + 1);
                    uri = uri.substring(0, queryStart);
                }

                Map<? super String, ? super String> map = Maps.parse(null, queryString, '&', '=', false);
                Map<Object, Object> arg = new HashMap<>();
                arg.putAll(this._arg);
                arg.putAll(map);

                return new DefaultTemplate(arg, uri);
            }

            return null;
        }

        public Template resolve(Object o) {
            if (isNotBlank(this._templateName)) {
                CollectionTemplate _inst = CollectionTemplate.this;
                return lookupTemplate(_inst._forEach, _inst._forEach, this._templateName);
            }

            return getTemplateURI();
        }

        private class DefaultTemplate implements Template {

            private Map<String, Object> arg;
            private String uri;

            @SuppressWarnings("unchecked")
            private DefaultTemplate(Map arg, String uri) {
                this.arg = arg;
                this.uri = uri;
            }

            @Override
            public Component[] create(Component parent, Component insertBefore, VariableResolver resolver, Composer composer) {
                Execution exec = Executions.getCurrent();
                return exec.createComponents(
                    this.uri, parent, insertBefore, resolver, this.arg
                );
            }

            @Override
            public Map<String, Object> getParameters() {
                return this.arg;
            }
        }
    }
}
