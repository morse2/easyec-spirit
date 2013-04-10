package com.googlecode.easyec.spirit.mybatis.cache;

import com.googlecode.easyec.cache.CacheElement;
import com.googlecode.easyec.cache.CacheService;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.getBean;

/**
 * 缓存服务框架提供的Mybatis数据层的缓存支持类。
 *
 * @author JunJie
 */
public class CacheServiceCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final CacheService cacheService = getBean(CacheService.class);

    private String id;

    public CacheServiceCache(String id) {
        Assert.notNull(id, "Cache id cannot be null.");

        this.id = id;
        // 初始化缓存区
        boolean b = cacheService.addCacheIfAbsent(this.id);
        logger.debug("Result of adding cache. [{}]", b);
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        return Long.valueOf(cacheService.getStatistics(this.id).getObjectCount()).intValue();
    }

    public void putObject(Object key, Object value) {
        String thisKey = toKey(key);

        if (null != value) {
            boolean b = this.cacheService.put(this.id, thisKey, new DefaultCacheServiceCacheElement(thisKey, value));
            logger.debug("Result of putting into cache. [{}]", b);
        }
    }

    public Object getObject(Object key) {
        Object o = this.cacheService.get(this.id, toKey(key));
        if (null == o) return null;

        if (!(o instanceof CacheElement)) return o;
        return ((CacheElement) o).getValue();
    }

    public Object removeObject(Object key) {
        Object o = getObject(key);
        boolean b = this.cacheService.removeCache(this.id, String.valueOf(key.hashCode()));
        logger.debug("Result of removing from cache. [{}]", b);
        return o;
    }

    public void clear() {
        this.cacheService.removeAll(this.id);
    }

    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    private String toKey(Object k) {
        StringBuffer key = new StringBuffer();
        key.append(id).append("!").append(k);
        return String.valueOf(key.toString().hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((CacheServiceCache) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
