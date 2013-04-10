package com.googlecode.easyec.spirit.dao.identifier;

/**
 * 主键名称解析器类。
 *
 * @author JunJie
 */
public interface IdentifierNameResolver {

    /**
     * 执行分析传入的对象参数，并返回自增长序列的名字部分。
     *
     * @param o 表对象
     * @return 表名字
     */
    String populate(Object o);
}
