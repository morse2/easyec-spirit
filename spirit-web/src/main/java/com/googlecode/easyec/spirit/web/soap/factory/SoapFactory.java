package com.googlecode.easyec.spirit.web.soap.factory;

import com.googlecode.easyec.spirit.web.soap.Soap;

/**
 * SOAP协议封装工厂类。
 * 此类提供了XML与SOAP对象之间的转换方法。
 *
 * @author JunJie
 */
public interface SoapFactory {

    /**
     * SOAP 1.1协议的命名空间
     */
    String SOAP_NAMESPACE_11 = "http://schemas.xmlsoap.org/soap/envelope/";
    /**
     * SOAP 1.2协议的命名空间
     */
    String SOAP_NAMESPACE_12 = "http://www.w3.org/2003/05/soap-envelope";

    /**
     * 将SOAP对象编组成XML格式的字符串内容。
     *
     * @param soap 要编组的SOAP对象
     * @return XML格式的字符串
     */
    String asXml(Soap soap);

    /**
     * 将XML格式的字符串内容转换成SOAP对象。
     *
     * @param xml XML内容
     * @return SOAP对象实例
     */
    Soap asSoap(String xml);
}
