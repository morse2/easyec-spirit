package com.googlecode.easyec.spirit.mybatis.cache;

import com.googlecode.easyec.cache.CacheElement;

/**
 * 默认的、内部的支持缓存服务的缓存元素实现类。
 *
 * @author JunJie
 */
final class DefaultCacheServiceCacheElement extends CacheElement {

    private static final long serialVersionUID = -8228533696956137419L;

    DefaultCacheServiceCacheElement() {
        // default constructor
    }

    DefaultCacheServiceCacheElement(Object key, Object value) {
        super(key, value);
    }
}
