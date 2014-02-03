package com.googlecode.easyec.spirit.dao.id;

/**
 * 主键序列生成器接口类。
 * <p>
 * 该类负责解析传递的持久化对象，并确定需要执行哪个
 * <code>IdentifierGenerator</code>对象实例
 * </p>
 *
 * @author JunJie
 */
public interface SequenceGenerator {

    /**
     * 设置主键标识名字的转换类
     *
     * @param converter 主键标识转换类对象
     */
    void setIdentifierNameConverter(IdentifierNameConverter converter);

    /**
     * 处理参数对象中主键的方法。
     *
     * @param arg 参数对象
     * @throws Exception
     */
    void process(Object arg) throws Exception;
}
