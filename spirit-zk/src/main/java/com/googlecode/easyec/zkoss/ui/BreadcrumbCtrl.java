package com.googlecode.easyec.zkoss.ui;

/**
 * 面包屑控制功能的接口类
 *
 * @author junjie
 */
public interface BreadcrumbCtrl {

    /**
     * 面包屑ID键值
     */
    String BC_ID = "$BreadCrumb$";

    /**
     * 面包屑标签名
     */
    String BC_LABEL = "bc-label";

    /**
     * 返回当前页面的面包屑信息。
     * 如果页面没有配置面包屑，
     * 则返回null
     *
     * @return <code>Breadcrumb</code>
     */
    Breadcrumb getBreadcrumb();
}
