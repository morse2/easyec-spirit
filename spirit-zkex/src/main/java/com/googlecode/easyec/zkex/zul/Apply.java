package com.googlecode.easyec.zkex.zul;

import org.zkoss.bind.Binder;
import org.zkoss.bind.impl.BinderUtil;
import org.zkoss.bind.sys.ReferenceBinding;
import org.zkoss.lang.Objects;
import org.zkoss.util.Maps;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.ext.DynamicPropertied;
import org.zkoss.zk.ui.metainfo.impl.ShadowDefinitionImpl;
import org.zkoss.zk.ui.sys.ComponentCtrl;
import org.zkoss.zk.ui.sys.PropertyAccess;
import org.zkoss.zk.ui.sys.StringPropertyAccess;
import org.zkoss.zk.ui.util.Template;

import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unchecked")
public class Apply extends TemplateBasedShadowElement implements DynamicPropertied {

    private static final String REFERENCE_SET = "$REF_SET$";
    private static final long serialVersionUID = 2272099878848265140L;
    private static HashMap<String, PropertyAccess> _properties = new HashMap(2);

    private String _template = "";
    private String _templateURI;
    protected Map<String, Object> _props;

    static {
        _properties.put("template", new StringPropertyAccess() {

            private static final long serialVersionUID = -5265919955968096386L;

            public void setValue(Component cmp, String value) {
                ((Apply) cmp).setTemplate(value);
            }

            public String getValue(Component cmp) {
                return ((Apply) cmp).getTemplate();
            }
        });

        _properties.put("templateURI", new StringPropertyAccess() {

            private static final long serialVersionUID = 7247689603549660390L;

            public void setValue(Component cmp, String value) {
                ((Apply) cmp).setTemplateURI(value);
            }

            public String getValue(Component cmp) {
                return ((Apply) cmp).getTemplateURI();
            }
        });
    }

    public Apply() {
        init();
    }

    private void init() {
        this._props = new LinkedHashMap<>();
    }

    public String getTemplate() {
        return this._template;
    }

    public void setTemplate(String template) {
        if (isNotBlank(template) && isNotBlank(_templateURI)) {
            throw new UiException("Can not set template and template uri in the same time. Set template uri as null or empty string first.");
        }

        if (!Objects.equals(this._template, template)) {
            this._template = template;
            this._dirtyBinding = true;
        }
    }

    public void setTemplateURI(String templateURI) {
        if (isNotBlank(templateURI) && isNotBlank(_template)) {
            throw new UiException("Can not set template and template uri in the same time. Set template as null or empty string first.");
        }

        if (!Objects.equals(this._templateURI, templateURI)) {
            this._templateURI = templateURI;
            this._dirtyBinding = true;
        }
    }

    public String getTemplateURI() {
        return this._templateURI;
    }

    protected Template resolveTemplate() {
        return isBlank(_templateURI)
            ? lookupTemplate(this, this, getTemplate())
            : null;
    }

    private Template lookupTemplate(Component comp, Apply base, String name) {
        return Templates.lookup(comp, base, name);
    }

    public Object resolveVariable(Component child, String name, boolean recurse) {
        Object result = this._props.get(name);
        return null == result
            ? super.resolveVariable(child, name, recurse)
            : result;
    }

    protected void compose(Component host) {
        Execution exec = Executions.getCurrent();
        if (null == exec) {
            throw new IllegalStateException("No execution available");
        }

        Template _t = resolveTemplate();
        if (null != _t) {
            exec.pushArg(this._props);

            try {
                _t.create(host, getNextInsertionComponentIfAny(), null, null);
            } finally {
                exec.popArg();
            }
        }

        String templateURI = this._templateURI;
        if (null == templateURI) {
            templateURI = ((ShadowDefinitionImpl) getDefinition()).getTemplateURI();
        }

        if (null != templateURI) {
            String uri = templateURI;
            int queryStart = uri.indexOf('?');
            String queryString = null;
            if (queryStart >= 0) {
                queryString = uri.substring(queryStart + 1);
                uri = uri.substring(0, queryStart);
            }

            Map<? super String, ? super String> map = Maps.parse(null, queryString, '&', '=', false);
            Map<Object, Object> arg = new HashMap<>();
            arg.putAll(this._props);
            arg.putAll(map);

            exec.createComponents(uri, host, getNextInsertionComponentIfAny(), null, arg);
        }

        Set<String> refs = (Set<String>) getAttribute(REFERENCE_SET);
        if (isNotEmpty(refs)) {
            Binder binder = BinderUtil.getBinder(this);
            for (Component comp : getDistributedChildren()) {
                copyReferenceBinding(comp, refs, binder);
            }
        }
    }

    private void copyReferenceBinding(Component comp, Set<String> refs, Binder binder) {
        for (String key : refs) {
            addReferenceBinding(comp, key, (ReferenceBinding) getAttribute(key));
        }
    }

    private void addReferenceBinding(Component comp, String attr, ReferenceBinding binding) {
        comp.setAttribute(attr, binding);

        Set<String> refs = (Set) comp.getAttribute(REFERENCE_SET, COMPONENT_SCOPE);
        if (null == refs) {
            refs = new HashSet<>();
            comp.setAttribute(REFERENCE_SET, refs);
        }

        refs.add(attr);
    }

    protected boolean isEffective() {
        ComponentCtrl compCtrl = this;
        if (isNotEmpty(compCtrl.getAnnotatedProperties()) && !isBindingReady()) return false;
        boolean hasTemplate = (!"".equals(this._template)) || (getTemplate("") != null);
        return (hasTemplate) || (this._templateURI != null) || (((ShadowDefinitionImpl) getDefinition()).getTemplateURI() != null);
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
        this._dirtyBinding = true;
        this._props.put(name, value);
    }

    public PropertyAccess getPropertyAccess(String prop) {
        PropertyAccess pa = _properties.get(prop);
        return null == pa
            ? super.getPropertyAccess(prop)
            : pa;
    }

    public Object clone() {
        Apply clone = (Apply) super.clone();
        clone.init();
        clone._props.putAll(this._props);
        return clone;
    }
}
