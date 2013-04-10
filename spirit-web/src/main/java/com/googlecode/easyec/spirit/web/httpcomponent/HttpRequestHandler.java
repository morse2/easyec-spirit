package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.client.HttpClient;

/**
 * HTTP请求处理对象。
 *
 * @author JunJie
 */
public interface HttpRequestHandler<T> {

    /**
     * 处理方法。可以返回处理的结果
     *
     * @param httpClient {@link org.apache.http.client.HttpClient}
     * @return T
     * @throws Exception 异常信息
     */
    T process(HttpClient httpClient) throws Exception;
}
