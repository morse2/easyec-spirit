package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.URI;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 抽象的HTTP GET方式的请求调用的处理类
 *
 * @author JunJie
 */
public abstract class AbstractHttpGetRequestHandler<T, B> extends AbstractHttpRequestHandler<T, B> {

    public AbstractHttpGetRequestHandler(StreamObjectFactory objectFactory, String baseUri) {
        super(objectFactory, baseUri);
    }

    public AbstractHttpGetRequestHandler(StreamObjectFactory objectFactory, String baseUri, B bean) {
        super(objectFactory, baseUri, bean);
    }

    @Override
    protected HttpUriRequest createHttpUriRequest() throws Exception {
        return new HttpGet(createURI());
    }

    @Override
    protected void beforeHttpExecution(HttpUriRequest request) throws Exception {
        super.beforeHttpExecution(request);

        handleHttpHeaders(request);
    }

    @Override
    protected URI createURI() throws Exception {
        StringBuffer uri = new StringBuffer();
        uri.append(getBaseUri()).append(getBusinessUri());

        String qs = createQueryString();
        if (isNotBlank(qs)) {
            uri.append("?").append(qs);
        }

        return new URI(uri.toString());
    }

    /**
     * 处理HTTP调用的头信息
     *
     * @param request HTTP URI请求对象实例
     * @throws Exception
     */
    protected void handleHttpHeaders(HttpUriRequest request) throws Exception {
        // no op
    }

    /**
     * 创建GET请求的查询字符串参数
     *
     * @throws IOException
     */
    protected String createQueryString() throws IOException {
        return null;
    }
}
