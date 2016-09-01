package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * HTTP请求对象默认实现。
 *
 * @author JunJie
 */
@Deprecated
final class DefaultHttpRequest implements HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpRequest.class);
    private ClientConnectionManager clientConnectionManager;

    /**
     * 通过{@link ClientConnectionManager}实例，构造此对象实例。
     *
     * @param clientConnectionManager {@link ClientConnectionManager}实例对象
     */
    DefaultHttpRequest(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
    }

    public <T> T request(HttpRequestHandler<T> handler) throws Exception {
        Assert.notNull(handler, "HttpRequestHandler is null.");

        try {
            return handler.process(new DefaultHttpClient(clientConnectionManager));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw e;
        }
    }

    public void requestWithoutResult(HttpRequestNoResultHandler handler) throws Exception {
        request(handler);
    }
}
