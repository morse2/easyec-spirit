package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;

/**
 * 抽象的基于HTTP POST方式的接口请求调用处理类
 *
 * @author JunJie
 */
public abstract class AbstractHttpPostRequestHandler<T, B> extends AbstractHttpGetRequestHandler<T, B> {

    public AbstractHttpPostRequestHandler(StreamObjectFactory objectFactory, String baseUri, B bean) {
        super(objectFactory, baseUri, bean);
    }

    @Override
    protected HttpUriRequest createHttpUriRequest() throws Exception {
        return new HttpPost(createURI());
    }

    @Override
    protected void beforeHttpExecution(HttpUriRequest request) throws Exception {
        super.beforeHttpExecution(request);

        /* 创建POST实体对象 */
        HttpEntity entity = createPostEntity();
        Assert.notNull(entity, "HttpEntity is null.");

        // 设置POST的实体对象内容
        ((HttpPost) request).setEntity(entity);
    }

    /**
     * 创建需要被POST出去的内容
     *
     * @return POST的内容信息
     * @throws Exception
     */
    abstract protected HttpEntity createPostEntity() throws Exception;
}
