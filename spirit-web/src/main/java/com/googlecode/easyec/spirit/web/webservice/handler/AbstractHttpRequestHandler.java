package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.utils.BeanUtils;
import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import com.googlecode.easyec.spirit.web.webservice.support.HttpResponseContent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * 抽象的HTTP调用处理实现类.
 * 该类为HTTP请求调用提供了
 * 一般的执行过程及方法.子类
 * 可以较集中地关心其业务逻辑
 * 的实现.
 *
 * @author JunJie
 */
public abstract class AbstractHttpRequestHandler<T, B> implements HttpRequestHandler<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private StreamObjectFactory objectFactory;
    private String baseUri;
    private B bean;

    private String encoding = "UTF-8";

    public AbstractHttpRequestHandler(StreamObjectFactory objectFactory, String baseUri) {
        this(objectFactory, baseUri, null);
    }

    public AbstractHttpRequestHandler(StreamObjectFactory objectFactory, String baseUri, B bean) {
        Assert.notNull(objectFactory, "StreamObjectFactory is null.");
        Assert.notNull(baseUri, "Base URI is null.");

        this.objectFactory = objectFactory;
        this.baseUri = baseUri;
        this.bean = bean;
    }

    @Override
    public T process(HttpClient httpClient) throws Exception {
        HttpUriRequest request = createHttpUriRequest();
        logger.debug("HttpUriRequest method: [{}].", request.getMethod());

        /* 执行调用前的方法 */
        beforeHttpExecution(request);

        return httpClient.execute(
            request, createHttpResponseHandler()
        );
    }

    /**
     * 返回流对象工厂类的实例对象
     */
    public StreamObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * 返回目标服务器的基础URI
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * 当前请求处理对象的业务对象实例
     */
    public B getBean() {
        return bean;
    }

    /**
     * 返回当前请求数据内容的字符集编码.
     * 默认返回UTF-8
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置当前请求数据内容的字符编码
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 在HTTP调用之前执行的方法
     *
     * @param request HTTP URI请求对象实例
     * @see #createHttpUriRequest()
     */
    protected void beforeHttpExecution(HttpUriRequest request) throws Exception {
        // no op
    }

    protected ResponseHandler<T> createHttpResponseHandler() {
        return new InternalHttpResponseHandler();
    }

    /**
     * 检查当前HTTP调用后的相应代码是否是成功,
     * 如果不成功,则抛出相应的异常
     *
     * @param response <code>HttpResponse</code>对象
     * @throws HttpResponseException
     */
    protected void checkResponseStatus(HttpResponse response) throws HttpResponseException {
        StatusLine status = response.getStatusLine();
        logger.debug("Status of response: [{}].", status.getStatusCode());

        if (status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpResponseException(
                status.getStatusCode(),
                status.getReasonPhrase()
            );
        }
    }

    /**
     * 创建一个HTTP URI请求对象实例
     */
    abstract protected HttpUriRequest createHttpUriRequest() throws Exception;

    /**
     * 返回业务URI地址
     */
    abstract protected String getBusinessUri();

    /**
     * 创建GET请求的URI信息
     *
     * @throws Exception
     */
    abstract protected URI createURI() throws Exception;

    /**
     * 如果返回值为200, 则进入此方法.
     * 用户可以自定义解析返回的内容信息.
     *
     * @param content <code>HttpResponseContent</code>对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected T doIn200(HttpResponseContent content) throws IOException {
        if (getObjectFactory().accept(content.getContentType())) {
            return (T) getObjectFactory().readValue(
                content.getContent(),
                BeanUtils.findGenericType(this, 0)
            );
        }

        return null;
    }

    /**
     * 在方法{@link #doIn200(HttpResponseContent)}
     * 执行之后, 且当返回值不为null时候, 此方法才会被调用.
     *
     * @param target 目标返回的对象实例
     */
    protected void afterDoIn200(T target) {
        // no op
    }

    /**
     * 如果返回值不为200, 则进入此方法.
     * 进入此方法, 表示调用是不成功的.
     *
     * @param content <code>HttpResponseContent</code>对象
     * @param code    HTTP响应代码
     * @throws IOException
     */
    protected void doInOtherCode(HttpResponseContent content, int code) throws IOException {
        // no op
    }

    /**
     * 解析HTTP响应数据内容的方法
     *
     * @param response <code>HttpResponse</code>对象
     */
    protected HttpResponseContent resolveHttpResponseContent(HttpResponse response) throws IOException {
        /* 获取服务器响应的内容 */
        HttpEntity entity = response.getEntity();
        if (entity == null) return null;

        return new InternalHttpResponseContent(
            EntityUtils.toByteArray(entity),
            ContentType.getOrDefault(entity)
        );
    }

    // ----- private class here
    private class InternalHttpResponseContent implements HttpResponseContent {

        private static final long serialVersionUID = 1236157964527628671L;

        private ContentType contentType;
        private byte[] bs;

        InternalHttpResponseContent(byte[] bs, ContentType contentType) {
            this.bs = bs;
            this.contentType = contentType;
        }

        @Override
        public byte[] getContent() {
            return bs;
        }

        @Override
        public String getContentType() {
            return contentType.getMimeType();
        }

        @Override
        public Charset getCharset() {
            return contentType.getCharset();
        }
    }

    private class InternalHttpResponseHandler implements ResponseHandler<T> {

        @Override
        public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            try {
                checkResponseStatus(response);

                return doIn200(
                    resolveHttpResponseContent(response)
                );
            } catch (HttpResponseException e) {
                logger.warn(e.getMessage(), e);

                doInOtherCode(
                    resolveHttpResponseContent(response),
                    e.getStatusCode()
                );
            }

            return null;
        }
    }
}
