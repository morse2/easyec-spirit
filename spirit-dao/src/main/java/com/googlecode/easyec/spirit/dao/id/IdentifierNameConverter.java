package com.googlecode.easyec.spirit.dao.id;

/**
 * 主键名称转换类。
 *
 * @author JunJie
 */
public interface IdentifierNameConverter {

    /**
     * 执行分析传入的对象参数，并返回自增长序列的名字部分。
     *
     * @param obj 表对象
     * @return 表名字
     */
    String populate(Object obj);
}
