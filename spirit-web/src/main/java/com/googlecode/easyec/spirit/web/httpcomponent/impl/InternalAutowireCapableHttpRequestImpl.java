package com.googlecode.easyec.spirit.web.httpcomponent.impl;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequest;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestNoResultHandler;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.autowireBeanProperties;
import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.isInitialized;

/**
 * 自动适配注入Spring Bean的<code>HttpRequest</code>对象的内部实现类
 *
 * @author JunJie
 */
final class InternalAutowireCapableHttpRequestImpl implements HttpRequest {

    private HttpRequest httpRequest;

    InternalAutowireCapableHttpRequestImpl(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public <T> T request(HttpRequestHandler<T> handler) throws Exception {
        if (isInitialized()) autowireBeanProperties(handler, false);

        return httpRequest.request(handler);
    }

    @Override
    public void requestWithoutResult(HttpRequestNoResultHandler handler) throws Exception {
        request(handler);
    }
}
