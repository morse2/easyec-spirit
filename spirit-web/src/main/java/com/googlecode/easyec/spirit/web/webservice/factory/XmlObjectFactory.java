package com.googlecode.easyec.spirit.web.webservice.factory;

/**
 * 支持XML与对象之间互转的工厂接口类
 *
 * @author JunJie
 */
public interface XmlObjectFactory {

    /**
     * 将对象编组成XML格式的字符串内容。
     *
     * @param obj 对象实例
     * @return XML格式的字符串
     */
    String asXml(Object obj);

    /**
     * 将XML格式的字符串内容转换成实体对象。
     *
     * @param xml XML内容
     * @return 对象实例
     */
    Object asObject(String xml);
}
