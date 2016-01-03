package com.googlecode.easyec.spirit.web.webservice.support;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * 表示HTTP调用后返回的响应的内容对象类
 *
 * @author JunJie
 */
public interface HttpResponseContent extends Serializable {

    /**
     * 返回响应数据内容
     */
    byte[] getContent();

    /**
     * 返回响应数据内容的类型
     */
    String getContentType();

    /**
     * 返回响应内容的字符集
     */
    Charset getCharset();
}
