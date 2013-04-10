package com.googlecode.easyec.spirit.mybatis.cache.interceptor;

import com.googlecode.easyec.cache.CacheElement;
import com.googlecode.easyec.cache.annotation.CachePut;
import com.googlecode.easyec.cache.annotation.Cacheable;
import com.googlecode.easyec.cache.interceptor.CacheAspectInterceptor;
import com.googlecode.easyec.cache.interceptor.SpelCacheCallback;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.loader.WriteReplaceInterface;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 实现MyBatis框架的缓存，执行拦截的类。
 *
 * @author JunJie
 */
public class MybatisCacheAspectInterceptor extends CacheAspectInterceptor {

    @Override
    protected SpelCacheCallback createCacheableCallback(
            final Cacheable cacheable, final ProceedingJoinPoint jp, final Method method, final Class<?> targetClass
    ) {
        return new SpelCacheCallback(cacheable.value(), cacheable.key()) {

            @Override
            public Object execute() throws Throwable {
                // 判断缓存列表是否为空，不为空则操作缓存
                if (ArrayUtils.isNotEmpty(getCaches())) {
                    // 解析当前缓存的KEY
                    String k = this.parseCacheKey(method, jp.getArgs(), jp.getTarget(), targetClass);
                    // 当前缓存的KEY不为空，则继续操作缓存
                    if (StringUtils.isNotBlank(k)) {
                        for (String name : getCaches()) {
                            Object o = getCacheService().get(name, k);

                            // 如果从缓存中获取的对象不为空，
                            // 则直接返回此对象
                            if (null != o) {
                                if (!(o instanceof CacheElement)) return o;
                                return ((CacheElement) o).getValue();
                            }
                        }

                        Object o = jp.proceed(jp.getArgs());

                        if (null != o) {
                            // check whether is CGLIB class, or
                            // check whether is JDK proxy class
                            if ((ClassUtils.isCglibProxy(o) || Proxy.isProxyClass(o.getClass()))
                                    && (o instanceof WriteReplaceInterface)) {
                                o = ((WriteReplaceInterface) o).writeReplace();
                            }

                            CacheElement e = createCacheElement(k, o, cacheable.timeToLive(), cacheable.timeToIdle());

                            for (String cache : getCaches()) {
                                getCacheService().put(cache, k, e);
                            }
                        }

                        return o;
                    }
                }

                return jp.proceed(jp.getArgs());
            }
        };
    }

    @Override
    protected SpelCacheCallback createCachePutCallback(
            final CachePut cachePut, final ProceedingJoinPoint jp, final Method method, final Class<?> targetClass
    ) {
        return new SpelCacheCallback(cachePut.value(), cachePut.key()) {

            @Override
            public Object execute() throws Throwable {
                // 判断缓存列表是否为空，不为空则操作缓存
                if (ArrayUtils.isNotEmpty(getCaches())) {
                    // 解析当前缓存的KEY
                    String k = this.parseCacheKey(method, jp.getArgs(), jp.getTarget(), targetClass);
                    // 当前缓存的KEY不为空，则继续操作缓存
                    if (StringUtils.isNotBlank(k)) {
                        Object o = jp.proceed(jp.getArgs());

                        if (null != o) {
                            // check whether is CGLIB class, or
                            // check whether is JDK proxy class
                            if ((ClassUtils.isCglibProxy(o) || Proxy.isProxyClass(o.getClass()))
                                    && (o instanceof WriteReplaceInterface)) {
                                o = ((WriteReplaceInterface) o).writeReplace();
                            }

                            CacheElement e = createCacheElement(k, o, cachePut.timeToLive(), cachePut.timeToIdle());

                            for (String cache : getCaches()) {
                                getCacheService().put(cache, k, e);
                            }
                        }

                        return o;
                    }
                }

                return jp.proceed(jp.getArgs());
            }
        };
    }
}
