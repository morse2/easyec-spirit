package com.googlecode.easyec.spirit.mybatis.cache;

import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * Mybatis日志型的缓存服务类。
 *
 * @author JunJie
 */
public class LoggingCacheService extends LoggingCache {

    public LoggingCacheService(String id) {
        super(new CacheServiceCache(id));
    }
}
