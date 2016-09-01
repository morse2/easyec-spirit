package com.googlecode.easyec.spirit.web.httpcomponent.impl;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequest;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestNoResultHandler;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <code>HttpRequest</code>的内部实现类
 *
 * @author JunJie
 */
final class InternalHttpRequestImpl implements HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(InternalHttpRequestImpl.class);
    private HttpClient httpClient;

    InternalHttpRequestImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public <T> T request(HttpRequestHandler<T> handler) throws Exception {
        Assert.notNull(handler, "HttpRequestHandler is null.");

        try {
            return handler.process(httpClient);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public void requestWithoutResult(HttpRequestNoResultHandler handler) throws Exception {
        request(handler);
    }
}
