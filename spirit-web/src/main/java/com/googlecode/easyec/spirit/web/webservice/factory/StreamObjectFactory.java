package com.googlecode.easyec.spirit.web.webservice.factory;

/**
 * 流对象工厂接口类.
 * 子类实现HTTP的请求及响应内容的数据转换
 * 的方法的实现.
 *
 * @author JunJie
 */
public interface StreamObjectFactory {

    /**
     * 将对象编码成具体流的内容的字符串数据
     *
     * @param obj 对象实例
     * @return 字符串格式的流数据
     */
    String writeValue(Object obj);

    /**
     * 将字符串格式的流数据转换成相应的业务对象实例
     *
     * @param s         流数据内容
     * @param classType 类类型
     * @return 业务对象实例
     */
    <T> T readValue(String s, Class<T> classType);

    /**
     * 标识HTTP请求的数据格式内容的类型是否被此工厂实例所接收
     *
     * @param contentType 数据内容类型
     */
    boolean accept(String contentType);
}
