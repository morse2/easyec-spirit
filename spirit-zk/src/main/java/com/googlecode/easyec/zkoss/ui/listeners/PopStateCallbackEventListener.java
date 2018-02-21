package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.events.PopStateEvent;
import com.googlecode.easyec.zkoss.ui.pushstate.PopState;
import com.googlecode.easyec.zkoss.ui.pushstate.PushState;
import org.zkoss.zk.ui.event.SerializableEventListener;

/**
 * 支持浏览器历史记录前进、
 * 后退操作的回调事件监听类
 *
 * @author junjie
 */
public abstract class PopStateCallbackEventListener implements SerializableEventListener<PopStateEvent> {

    private static final long serialVersionUID = 107873968769416518L;

    @Override
    public void onEvent(PopStateEvent event) throws Exception {
        PopState state = PushState.poll(event.getState());
        if (state != null) evaluate(state);
    }

    /**
     * 计算<code>PopState</code>对象值的方法。
     *
     * @param state <code>PopState</code>
     * @throws Exception 异常信息
     */
    abstract protected void evaluate(PopState state) throws Exception;
}
