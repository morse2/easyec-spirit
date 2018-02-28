package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.zkoss.zk.ui.Component;

public class StepOutWithPagingEventListener extends StepOutUpperEventListener {

    private static final long serialVersionUID = -7852007164834371633L;
    private PagingExecutor pagingExecutor;

    public StepOutWithPagingEventListener(Component curComp, Component parent) {
        super(curComp, parent);
    }

    public void setPagingExecutor(PagingExecutor pagingExecutor) {
        this.pagingExecutor = pagingExecutor;
    }

    @Override
    protected void doStepOut(StepOutEvent event) {
        super.doStepOut(event);

        pagingExecutor.firePaging(
            pagingExecutor.getPaging().getActivePage() + 1
        );
    }
}
