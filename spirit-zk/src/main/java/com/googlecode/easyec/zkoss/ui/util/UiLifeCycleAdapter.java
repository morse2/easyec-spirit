package com.googlecode.easyec.zkoss.ui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.util.UiLifeCycle;

/**
 * <code>UiLifeCycle</code>
 * 适配器类
 *
 * @author junjie
 */
public class UiLifeCycleAdapter implements UiLifeCycle {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterShadowAttached(ShadowElement shadow, Component host) {
        logger.trace("[afterShadowAttached] method has been called.");
    }

    @Override
    public void afterShadowDetached(ShadowElement shadow, Component prevhost) {
        logger.trace("[afterShadowDetached] method has been called.");
    }

    @Override
    public void afterComponentAttached(Component comp, Page page) {
        logger.trace("[afterComponentAttached] method has been called.");
    }

    @Override
    public void afterComponentDetached(Component comp, Page prevpage) {
        logger.trace("[afterComponentDetached] method has been called.");
    }

    @Override
    public void afterComponentMoved(Component parent, Component child, Component prevparent) {
        logger.trace("[afterComponentMoved] method has been called.");
    }

    @Override
    public void afterPageAttached(Page page, Desktop desktop) {
        logger.trace("[afterPageAttached] method has been called.");
    }

    @Override
    public void afterPageDetached(Page page, Desktop prevdesktop) {
        logger.trace("[afterPageDetached] method has been called.");
    }
}
