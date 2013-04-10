package com.googlecode.easyec.spirit.web.httpcomponent;

/**
 * HTTP请求封装类。使用此类，可以执行一个HTTP请求，并处理具体的业务逻辑。
 *
 * @author JunJie
 */
public interface HttpRequest {

    /**
     * 执行一个HTTP请求。
     * 调用此方法，必须使用{@link HttpRequestHandler}来处理请求的内容。
     *
     * @param handler {@link HttpRequestHandler}实例对象
     * @param <T>     枚举类型
     * @return 返回枚举类型变量实例
     * @throws Exception
     */
    <T> T request(HttpRequestHandler<T> handler) throws Exception;

    /**
     * 执行一个HTTP请求。
     * 调用此方法，必须使用{@link HttpRequestNoResultHandler}来处理请求的内容。
     *
     * @param handler {@link HttpRequestNoResultHandler}实例对象
     * @throws Exception
     */
    void requestWithoutResult(HttpRequestNoResultHandler handler) throws Exception;
}
