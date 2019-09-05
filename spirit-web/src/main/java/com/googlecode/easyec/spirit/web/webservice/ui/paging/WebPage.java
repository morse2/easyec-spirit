package com.googlecode.easyec.spirit.web.webservice.ui.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;

import java.util.Map;

/**
 * @author JunJie
 */
public interface WebPage extends Page {

    /**
     * 获取当前设置的查询条件
     *
     * @return 查询条件
     */
    Map<String, String[]> getParameters();

    /**
     * 设置查询条件
     *
     * @param params 查询条件
     */
    void setParameters(Map<String, String[]> params);
}
