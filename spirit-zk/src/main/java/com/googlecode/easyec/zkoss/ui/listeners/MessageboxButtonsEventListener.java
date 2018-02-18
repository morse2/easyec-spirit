package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.oper.ButtonsOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.Messagebox;

/**
 * 消息框中按钮事件的适配器实现类。
 * 如果事件名字为空，则该类不做任何事情。
 *
 * @author junjie
 */
public class MessageboxButtonsEventListener implements SerializableEventListener<Event> {

    private static final long serialVersionUID = 9139339367903936696L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ButtonsOperation buttonsOperation;

    public MessageboxButtonsEventListener(ButtonsOperation buttonsOperation) {
        Assert.notNull(buttonsOperation, "ButtonsOperation mustn't be null.");
        this.buttonsOperation = buttonsOperation;
    }

    @Override
    public final void onEvent(Event event) throws Exception {
        String btn = event.getName();
        logger.debug("Button name is: [{}].", btn);

        if (StringUtils.isBlank(btn)) {
            logger.trace("No button name was present. Ignore operations else.");

            return;
        }

        if (Messagebox.ON_YES.equals(btn)) {
            buttonsOperation.onYes(event);
        } else if (Messagebox.ON_OK.equals(btn)) {
            buttonsOperation.onOK(event);
        } else if (Messagebox.ON_NO.equals(btn)) {
            buttonsOperation.onNo(event);
        } else if (Messagebox.ON_CANCEL.equals(btn)) {
            buttonsOperation.onCancel(event);
        } else if (Messagebox.ON_ABORT.equals(btn)) {
            buttonsOperation.onAbort(event);
        } else if (Messagebox.ON_IGNORE.equals(btn)) {
            buttonsOperation.onIgnore(event);
        } else if (Messagebox.ON_RETRY.equals(btn)) {
            buttonsOperation.onRetry(event);
        } else buttonsOperation.onOther(event);
    }
}
