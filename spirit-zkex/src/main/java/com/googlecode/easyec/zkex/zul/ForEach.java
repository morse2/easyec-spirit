package com.googlecode.easyec.zkex.zul;

import org.springframework.util.Assert;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.*;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForEach extends TemplateBasedShadowElement {

    private static final long serialVersionUID = 6008601581241519707L;
    private AuxInfo _auxinf;
    private ForEachConverter _conv;
    protected ForEachListDataListener _dataListener;

    private static HashMap<String, PropertyAccess> _properties = new HashMap<>(6);

    static {
        _properties.put("items", new ObjectPropertyAccess() {

            private static final long serialVersionUID = 3101783987699465112L;

            public void setValue(Component cmp, Object value) {
                ((ForEach) cmp).setItems(value);
            }

            public Object getValue(Component cmp) {
                return ((ForEach) cmp).getItems();
            }
        });
        _properties.put("begin", new IntPropertyAccess() {

            private static final long serialVersionUID = -2287340425284423180L;

            public void setValue(Component cmp, Integer value) {
                ((ForEach) cmp).setBegin(value);
            }

            public Integer getValue(Component cmp) {
                return ((ForEach) cmp).getBegin();
            }
        });
        _properties.put("end", new IntPropertyAccess() {

            private static final long serialVersionUID = -9054388138423711535L;

            public void setValue(Component cmp, Integer value) {
                ((ForEach) cmp).setEnd(value);
            }

            public Integer getValue(Component cmp) {
                return ((ForEach) cmp).getEnd();
            }
        });
        _properties.put("step", new IntPropertyAccess() {

            private static final long serialVersionUID = -4895725930839656535L;

            public void setValue(Component cmp, Integer value) {
                ((ForEach) cmp).setStep(value);
            }

            public Integer getValue(Component cmp) {
                return ((ForEach) cmp).getStep();
            }
        });
        _properties.put("var", new StringPropertyAccess() {

            private static final long serialVersionUID = 542854469519569261L;

            public void setValue(Component cmp, String value) {
                ((ForEach) cmp).setVar(value);
            }

            public String getValue(Component cmp) {
                return ((ForEach) cmp).getVar();
            }
        });
        _properties.put("varStatus", new StringPropertyAccess() {

            private static final long serialVersionUID = -2864659079127475031L;

            public void setValue(Component cmp, String value) {
                ((ForEach) cmp).setVarStatus(value);
            }

            public String getValue(Component cmp) {
                return ((ForEach) cmp).getVarStatus();
            }
        });
    }

    public ForEach() {
        this._auxinf = null;
        this._conv = new ForEachConverter();
    }

    public void setItems(Object items) {
        this._dirtyBinding = true;


        initAuxInfo().items = items;
    }

    public Object getItems() {
        return this._auxinf != null ? this._auxinf.items : null;
    }

    public void setBegin(int begin) {
        if (!Objects.equals(this._auxinf != null ? this._auxinf.begin : null, begin)) {
            initAuxInfo().begin = begin;
            this._dirtyBinding = true;
        }
    }

    public int getBegin() {
        return this._auxinf != null ? this._auxinf.begin : 0;
    }

    public void setEnd(int end) {
        if (!Objects.equals(this._auxinf != null ? this._auxinf.end : null, end)) {
            initAuxInfo().end = end;
            this._dirtyBinding = true;
        }
    }

    public int getEnd() {
        return (this._auxinf != null) && (this._auxinf.end > -1) ? this._auxinf.end : getItemsSize();
    }

    private int getItemsSize() {
        Object items = getItems();
        if ((items instanceof Collection))
            return ((Collection) items).size();
        if ((items instanceof Map))
            return ((Map) items).size();
        if ((items instanceof ListModelArray))
            return ((ListModelArray) items).getInnerArray().length;
        if ((items instanceof Object[]))
            return ((Object[]) items).length;
        if (((items instanceof Class)) && (Enum.class.isAssignableFrom((Class) items))) {
            return ((Class) items).getEnumConstants().length;
        }
        return 0;
    }

    public void setStep(int step) {
        if (step < 0)
            throw new IllegalArgumentException("'step' <= 0 [" + step + "]");
        if (!Objects.equals(this._auxinf != null ? this._auxinf.step : null, step)) {
            initAuxInfo().step = step;
            this._dirtyBinding = true;
        }
    }

    public int getStep() {
        return this._auxinf != null ? this._auxinf.step : 1;
    }

    public void setVar(String var) {
        if (!Objects.equals(this._auxinf != null ? this._auxinf.var : null, var)) {
            initAuxInfo().var = var;
            this._dirtyBinding = true;
        }
    }

    public String getVar() {
        return this._auxinf != null ? this._auxinf.var : "each";
    }

    public void setVarStatus(String varStatus) {
        if (!Objects.equals(this._auxinf != null ? this._auxinf.varStatus : null, varStatus)) {
            initAuxInfo().varStatus = varStatus;
            this._dirtyBinding = true;
        }
    }

    public String getVarStatus() {
        return this._auxinf != null ? this._auxinf.varStatus : "forEachStatus";
    }

    public ForEachConverter getDataConverter() {
        return this._conv;
    }

    private final AuxInfo initAuxInfo() {
        if (this._auxinf == null)
            this._auxinf = new AuxInfo();
        return this._auxinf;
    }

    private static class AuxInfo implements Serializable, Cloneable {

        private Object items;
        private int begin = 0;
        private int end = -1;
        private int step = 1;
        private String var = "each";
        private String varStatus = "forEachStatus";

        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
        }
    }

    protected boolean isEffective() {
        return null != getItems() || (null != _auxinf && _auxinf.begin <= _auxinf.end);
    }


    private void initDataListener(ListModel model, Component host) {
        if (this._dataListener == null) {
            this._dataListener = new ForEachListDataListener(this, host);
        } else {
            model.removeListDataListener(this._dataListener);
            this._dataListener = new ForEachListDataListener(this, host);
        }

        model.addListDataListener(this._dataListener);
    }

    protected void compose(Component host) {
        Assert.isTrue(getBegin() <= getEnd(), "End index must not be less than start index.");

        Object items = getItems();
        Template tm = getTemplate("");
        Object value = this._conv.coerceToUi(items);

        if (tm != null) {
            List<?> list = null;
            if ((value != null) && ((value instanceof List))) {
                list = (List) value;
            }

            boolean isUsingListModel = items instanceof ListModel;
            if (isUsingListModel) {

                initDataListener((ListModel) items, host);

                String forEachRenderedCompAttr = FOREACH_RENDERED_COMPONENTS + getUuid();

                if (host.hasAttribute(forEachRenderedCompAttr)) {
                    Object shadowInfo = ShadowElementsCtrl.getCurrentInfo();

                    try {
                        ShadowElementsCtrl.setCurrentInfo(this);
                        host.removeAttribute(forEachRenderedCompAttr);
                        List<Component> distributedChildren = getDistributedChildren();
                        for (int i = 0; i < distributedChildren.size(); i++) {
                            distributedChildren.remove(i--);
                        }
                    } finally {
                        ShadowElementsCtrl.setCurrentInfo(shadowInfo);
                    }
                }
            }

            ForEachRenderer renderer = new ForEachRenderer();
            renderer.render(this, host, tm, list, isUsingListModel);
        }
    }

    public boolean isDynamicValue() {
        return getItems() instanceof ListModel || super.isDynamicValue();
    }

    public PropertyAccess getPropertyAccess(String prop) {
        PropertyAccess pa = _properties.get(prop);
        return null == pa
            ? super.getPropertyAccess(prop)
            : pa;
    }
}
