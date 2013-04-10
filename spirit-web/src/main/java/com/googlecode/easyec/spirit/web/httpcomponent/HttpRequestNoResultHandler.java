package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.client.HttpClient;

/**
 * 无返回值的HTTP请求处理对象。
 *
 * @author JunJie
 */
public abstract class HttpRequestNoResultHandler implements HttpRequestHandler<Object> {

    public final Object process(HttpClient httpClient) throws Exception {
        processWithoutResult(httpClient);

        return null;
    }

    /**
     * 处理方法。此方法不返回处理的结果
     *
     * @param httpClient {@link org.apache.http.client.HttpClient}
     * @throws Exception 异常信息
     */
    abstract public void processWithoutResult(HttpClient httpClient) throws Exception;
}
