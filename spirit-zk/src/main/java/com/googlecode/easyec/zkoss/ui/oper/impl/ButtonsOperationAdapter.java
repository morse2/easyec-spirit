package com.googlecode.easyec.zkoss.ui.oper.impl;

import com.googlecode.easyec.zkoss.ui.oper.ButtonsOperation;
import org.zkoss.zk.ui.event.Event;

/**
 * 按钮操作的适配器类
 *
 * @author junjie
 */
public class ButtonsOperationAdapter implements ButtonsOperation {

    private static final long serialVersionUID = 325496552449829903L;

    @Override
    public void onYes(Event event) throws Exception {
        // no op
    }

    @Override
    public void onOK(Event event) throws Exception {
        // no op
    }

    @Override
    public void onNo(Event event) throws Exception {
        // no op
    }

    @Override
    public void onCancel(Event event) throws Exception {
        // no op
    }

    @Override
    public void onAbort(Event event) throws Exception {
        // no op
    }

    @Override
    public void onIgnore(Event event) throws Exception {
        // no op
    }

    @Override
    public void onRetry(Event event) throws Exception {
        // no op
    }

    @Override
    public void onOther(Event event) throws Exception {
        // no op
    }
}
