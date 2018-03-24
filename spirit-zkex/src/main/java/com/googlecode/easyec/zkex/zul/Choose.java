package com.googlecode.easyec.zkex.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlShadowElement;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.sys.ShadowElementsCtrl;
import org.zkoss.zk.ui.util.Template;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class Choose extends TemplateBasedShadowElement {

    private static final long serialVersionUID = 2912113084541365007L;
    private transient Otherwise _otherwise;
    private transient TemplateBasedShadowElement _evalTarget;
    private static String ATTR_RECREATE = "TriggerRecreateEvent";
    private static String ON_REEVALUATE_LATER = "onRecreateLater";

    public Choose() {
        init();
    }

    private void init() {
        addEventListener(
            ON_REEVALUATE_LATER,
            (SerializableEventListener<Event>) this::_doRecreateLater
        );
    }


    public void beforeChildAdded(Component child, Component refChild) {
        if ((!(child instanceof When)) && (!(child instanceof Otherwise))) {
            throw new UiException("Unsupported child for <choose>: " + child);
        }

        if ((child instanceof Otherwise)) {
            if ((this._otherwise != null) && (this._otherwise != child)) {
                throw new UiException("Only one <otherwise> child for <choose>: " + child);
            }

            if (refChild != null) {
                throw new UiException("Last child of <choose> should be <otherwise>: " + child);
            }

            this._otherwise = ((Otherwise) child);
        } else if ((this._otherwise != null) && (this._otherwise != refChild)) {
            throw new UiException("Last child of <choose> should be <otherwise>: " + child);
        }

        super.beforeChildAdded(child, refChild);
    }

    public void onChildRemoved(Component child) {
        if (child == this._otherwise) {
            this._otherwise = null;
        }

        super.onChildRemoved(child);
    }

    void triggerRecreateEvent() {
        if (!hasAttribute(ATTR_RECREATE)) {
            setAttribute(ATTR_RECREATE, Boolean.TRUE);
            Events.postEvent(55536, ON_REEVALUATE_LATER, this, null);
        }
    }

    private void reEvaluate() {
        List<HtmlShadowElement> children = getChildren();
        TemplateBasedShadowElement oldTarget = this._evalTarget;

        for (HtmlShadowElement shadow : children) {
            if ((shadow instanceof When)) {
                When when = (When) shadow;
                if (when.isEffective()) {
                    if (oldTarget != when) {
                        this._evalTarget = when;
                        Object shadowInfo = ShadowElementsCtrl.getCurrentInfo();

                        try {
                            ShadowElementsCtrl.setCurrentInfo(when);
                            when.recreateDirectly();
                        } finally {
                            ShadowElementsCtrl.setCurrentInfo(shadowInfo);
                        }
                    }
                } else {
                    if (isNotEmpty(when.getAnnotatedProperties()) && !when.isBindingReady()) {
                        break;
                    }
                }
            } else if (this._otherwise != null) {
                this._evalTarget = this._otherwise;
                Object shadowInfo = ShadowElementsCtrl.getCurrentInfo();

                try {
                    ShadowElementsCtrl.setCurrentInfo(this._otherwise);
                    this._otherwise.recreateDirectly();
                } finally {
                    ShadowElementsCtrl.setCurrentInfo(shadowInfo);
                }
            }
        }

        if ((oldTarget != null) && (this._evalTarget != oldTarget)) {
            oldTarget.getDistributedChildren().clear();
            oldTarget.getChildren().clear();
        }
    }

    protected void compose(Component host) {
        Template t = getTemplate("");
        if (t != null) {
            t.create(host, getNextInsertionComponentIfAny(), null, null);

            reEvaluate();
        }
    }

    protected Set<String> getDynamicKeys() {
        return Collections.emptySet();
    }

    public boolean isEffective() {
        return true;
    }

    protected void rebuildSubShadowTree() {
        List<TemplateBasedShadowElement> children = getChildren();

        boolean dynamicValue = false;
        if (!children.isEmpty()) {
            for (HtmlShadowElement se : children) {
                if (se.isDynamicValue()) {
                    dynamicValue = true;
                    break;
                }
            }

            if (!dynamicValue) {
                for (TemplateBasedShadowElement se : children) {
                    se.rebuildSubShadowTree();
                }
            }
        }

        if (!dynamicValue && !isDynamicValue()) {
            mergeSubTree();
            detach();
        }
    }


    public void detach() {
        super.detach();
        this._evalTarget = null;
    }

    public void mergeSubTree() {
        if (this._evalTarget != null) {
            List<HtmlShadowElement> children = this._evalTarget.getChildren();
            if (isEmpty(children)) return;

            for (HtmlShadowElement child : children) {
                Component previous = child.getPreviousInsertion();
                Component next = child.getNextInsertion();
                getParent().insertBefore(child, this);


                if ((previous != null) && (!(previous instanceof HtmlShadowElement))) {
                    Component newPrevious = child.getPreviousInsertion();
                    setPrevInsertion(previous, newPrevious);
                    setPrevInsertion(child, previous);
                }

                if ((next != null) && (!(next instanceof HtmlShadowElement))) {
                    Component newNext = child.getNextInsertion();
                    setPrevInsertion(newNext, next);
                    setPrevInsertion(next, child);
                }
            }
        }
    }

    public Object clone() {
        Choose clone = (Choose) super.clone();
        clone.init();
        clone.afterUnmarshal();
        return clone;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        init();
        afterUnmarshal();
    }

    private void afterUnmarshal() {
        List<Component> children = getChildren();
        for (Component next : children) {
            if ((next instanceof Otherwise)) {
                this._otherwise = ((Otherwise) next);

                break;
            }
        }
    }

    private void _doRecreateLater(Event event) {
        removeAttribute(ATTR_RECREATE);

        if (getShadowHostIfAny() == null) {
            Iterable<EventListener<? extends Event>> lsnrs = getEventListeners(ON_REEVALUATE_LATER);
            for (Iterator<EventListener<? extends Event>> it = lsnrs.iterator(); it.hasNext(); ) {
                it.remove();
            }

            return;
        }

        reEvaluate();
    }
}
