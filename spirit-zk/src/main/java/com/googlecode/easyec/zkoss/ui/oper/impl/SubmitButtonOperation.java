package com.googlecode.easyec.zkoss.ui.oper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;

/**
 * 提交类型按钮的操作基础类
 *
 * @author junjie
 */
public abstract class SubmitButtonOperation extends ButtonsOperationAdapter {

    private static final long serialVersionUID = -9080558156405929816L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onYes(Event event) throws Exception {
        _doInTemplate(event);
    }

    @Override
    public void onOK(Event event) throws Exception {
        _doInTemplate(event);
    }

    private void _doInTemplate(Event event) throws Exception {
        try {
            doBusiness(event);
            onSuccess(event);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            onError(e);
        }
    }

    /**
     * 操作成功后调用的方法
     *
     * @param event 事件对象
     */
    abstract protected void onSuccess(Event event);

    /**
     * 执行业务的方法
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    abstract protected void doBusiness(Event event) throws Exception;

    /**
     * 当遇到异常时候执行的方法
     *
     * @param e 异常对象
     * @throws Exception 可以抛出的异常信息
     */
    abstract protected void onError(Exception e) throws Exception;
}
