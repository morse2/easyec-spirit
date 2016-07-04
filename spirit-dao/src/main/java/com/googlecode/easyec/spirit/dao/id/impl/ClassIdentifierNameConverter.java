package com.googlecode.easyec.spirit.dao.id.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierNameConverter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 类对象主键名字转换实现类
 *
 * @author JunJie
 */
public class ClassIdentifierNameConverter implements IdentifierNameConverter {

    private String prefix = "SEQ_";
    private String suffix = "_ID";

    /**
     * 设置主键名称的前缀
     *
     * @param prefix 前缀名
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 设置主键名称的后缀
     *
     * @param suffix 后缀名
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String populate(Object obj) {
        if (null == obj) return "";

        StringBuffer sb = new StringBuffer();

        if (isNotBlank(prefix)) sb.append(prefix);
        sb.append(obj.getClass().getSimpleName());
        if (isNotBlank(suffix)) sb.append(suffix);

        return sb.toString();
    }
}
