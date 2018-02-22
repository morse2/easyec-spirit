package com.googlecode.easyec.zkoss.ui;

import java.io.Serializable;

/**
 * 表示面包屑对象类
 *
 * @author junjie
 */
public class Breadcrumb implements Serializable {

    /** 面包屑类型是Request */
    public static final int TYPE_URI = -1;
    /** 面包屑类型是Page */
    public static final int TYPE_PAGE = 1;
    private static final long serialVersionUID = -2583256487665258205L;

    private Breadcrumb parent;
    private String label;
    private String uri;
    private int type;

    public Breadcrumb(String label, String uri, int type) {
        this.label = label;
        this.uri = uri;
        this.type = type;
    }

    /**
     * 设置上一级面包屑信息
     *
     * @param parent <code>Breadcrumb</code>
     */
    public void setParent(Breadcrumb parent) {
        this.parent = parent;
    }

    /**
     * 返回面包屑显示的内容
     *
     * @return 显示文字
     */
    public String getLabel() {
        return label;
    }

    /**
     * 获取当前面包屑配置的uri
     *
     * @return page uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 返回上一级的面包屑信息。
     * 如果没有更高一级的面包屑，
     * 则返回null
     *
     * @return <code>Breadcrumb</code>
     */
    public Breadcrumb getParent() {
        return parent;
    }

    /**
     * 返回当前面包屑的类型。
     * <ul>
     * <li>-1表示是通过Request请求过来的页面</li>
     * <li>1表示通过ZK方式创建的页面</li>
     * </ul>
     *
     * @return 类型值
     */
    public int getType() {
        return type < 1 ? TYPE_URI : TYPE_PAGE;
    }
}
