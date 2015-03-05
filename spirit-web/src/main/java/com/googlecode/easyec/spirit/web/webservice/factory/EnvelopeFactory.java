package com.googlecode.easyec.spirit.web.webservice.factory;

import com.googlecode.easyec.spirit.web.webservice.Envelope;

/**
 * WebService的信封转换的工厂类
 *
 * @author JunJie
 */
@Deprecated
public interface EnvelopeFactory {

    /**
     * 将信封对象编组成XML格式的字符串内容。
     *
     * @param envelope 要编组的信封对象
     * @return XML格式的字符串
     */
    String asXml(Envelope envelope);

    /**
     * 将XML格式的字符串内容转换成信封对象。
     *
     * @param xml XML内容
     * @return 信封对象实例
     */
    Envelope asEnvelope(String xml);
}
