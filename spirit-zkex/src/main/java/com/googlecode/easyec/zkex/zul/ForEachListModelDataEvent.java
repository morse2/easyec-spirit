package com.googlecode.easyec.zkex.zul;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataEvent;

import java.io.Serializable;

public class ForEachListModelDataEvent extends ListDataEvent implements Serializable {

    private static final long serialVersionUID = -3498007114256748266L;

    public ForEachListModelDataEvent(ListModel<?> model, int type, int index0, int index1) {
        super(model, type, index0, index1);
    }

    public ForEachListModelDataEvent(ListDataEvent e) {
        super(e.getModel(), e.getType(), e.getIndex0(), e.getIndex1());
    }
}
