package com.googlecode.easyec.zkex.zul;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ShadowElementsCtrl;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;

import java.io.Serializable;
import java.util.List;

import static com.googlecode.easyec.zkex.zul.TemplateBasedShadowElement.FOREACH_RENDERED_COMPONENTS;
import static org.zkoss.zul.event.ListDataEvent.*;

public class ForEachListDataListener implements ListDataListener, Serializable {

    private static final long serialVersionUID = 1209551395228715314L;
    private final ForEach _forEachComp;
    private final Component _host;
    private final ForEachConverter _conv;
    private final Template _tm;

    public ForEachListDataListener(ForEach forEachComp, Component host) {
        this._forEachComp = forEachComp;
        this._host = host;
        this._conv = this._forEachComp.getDataConverter();
        this._tm = this._forEachComp.getTemplate("");
    }

    public void onChange(ListDataEvent event) {
        onListModelDataChange(new ForEachListModelDataEvent(event));
    }

    @SuppressWarnings("unchecked")
    private void onListModelDataChange(ListDataEvent event) {
        if ((this._host == null) || (this._host.getDesktop() == null)) {
            return;
        }

        ListModel<?> model = event.getModel();
        int type = event.getType();
        int index0 = event.getIndex0();
        int index1 = event.getIndex1();

        List<Component[]> feCompList = (List<Component[]>)
            this._host.getAttribute(
                FOREACH_RENDERED_COMPONENTS + this._forEachComp.getUuid()
            );

        int oldsz = CollectionUtils.size(feCompList);
        int newsz = model.getSize();
        Object shadowInfo = ShadowElementsCtrl.getCurrentInfo();

        try {
            ShadowElementsCtrl.setCurrentInfo(this._forEachComp);

            if (type == INTERVAL_ADDED) {
                int addedCount = index1 - index0 + 1;
                if (newsz - oldsz <= 0) {
                    throw new UiException("Adding causes a smaller list?");
                }

                if (oldsz + addedCount != newsz) {
                    index0 = oldsz;
                    index1 = newsz - 1;
                }

                renderModelData(this._host, model, index0, index1);
            } else if (type == CONTENTS_CHANGED) {
                if (index0 < 0) {
                    syncModel(this._host, model);
                } else {
                    for (int i = index0; i <= index1; i++) {
                        _cleanFeCompList(feCompList.get(i));
                        feCompList.remove(i);
                    }

                    renderModelData(this._host, model, index0, index1);
                }
            } else if (type == INTERVAL_REMOVED) {
                if (oldsz - newsz <= 0) {
                    throw new UiException("Removal causes a larger list?");
                }

                for (int i = index0; i <= index1; i++) {
                    _cleanFeCompList(feCompList.get(index0));
                    feCompList.remove(index0);
                }
            }
        } finally {
            ShadowElementsCtrl.setCurrentInfo(shadowInfo);
        }
    }


    private void syncModel(Component host, ListModel<?> model) {
        String forEachRenderedCompAttr = FOREACH_RENDERED_COMPONENTS + this._forEachComp.getUuid();
        List feCompList = (List) host.getAttribute(forEachRenderedCompAttr);
        if (null != feCompList) {
            host.removeAttribute(forEachRenderedCompAttr);

            List<Component> distributedChildren = _forEachComp.getDistributedChildren();
            for (int i = 0; i < distributedChildren.size(); i++) {
                distributedChildren.remove(i--);
            }
        }

        renderModelData(host, model, 0, model.getSize() - 1);
    }

    private void renderModelData(Component host, ListModel<?> model, int from, int to) {
        ForEachRenderer renderer = new ForEachRenderer();
        if (this._conv != null) {
            List data = (List) this._conv.coerceToUi(model);
            if (data != null) {
                int size = data.size();
                if (to >= size)
                    to = size - 1;
                renderer.render(
                    this._forEachComp, this._host, this._tm, from, to,
                    this._forEachComp.getStep(), data, true
                );
            }
        }
    }

    private void _cleanFeCompList(Component[] comps) {
        for (Component oldComp : comps) {
            if ((oldComp instanceof ShadowElement)) {
                ((ShadowElement) oldComp).getDistributedChildren().clear();
            }

            oldComp.detach();
        }
    }
}
