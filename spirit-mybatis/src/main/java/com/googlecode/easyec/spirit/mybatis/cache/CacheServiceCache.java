package com.googlecode.easyec.spirit.mybatis.cache;

import com.googlecode.easyec.caching.CacheService;
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

    /**
     * MyBatis缓存使用到的名字
     */
    public static final String MYBATIS_CACHE_NAME = "mybatisCache";

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private CacheService cacheService;
    private boolean initialized;

    private String id;

    public CacheServiceCache(String id) {
        Assert.notNull(id, "Mybatis Cache id of prefix cannot be null.");

        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        initialize();
        return Integer.MAX_VALUE;
    }

    public void putObject(Object key, Object value) {
        initialize();
        if (null != value) {
            boolean b = this.cacheService.put(MYBATIS_CACHE_NAME, key, value);
            logger.debug("Result of putting into cache. [{}]", b);
        }
    }

    public Object getObject(Object key) {
        initialize();
        return this.cacheService.get(MYBATIS_CACHE_NAME, key);
    }

    public Object removeObject(Object key) {
        Object o = getObject(key);
        boolean b = this.cacheService.removeValue(MYBATIS_CACHE_NAME, key);
        logger.debug("Result of removing from cache. [{}]", b);
        return o;
    }

    public void clear() {
        initialize();
        this.cacheService.removeAllValues(this.id);
    }

    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    /**
     * 初始化方法。
     * 在第一次使用到<code>CacheService</code>的时候，
     * 会被调用并初始化缓存实例
     */
    protected synchronized void initialize() {
        if (!isInitialized()) {
            // 初始化缓存区
            if (null == getCacheService()) {
                logger.debug("CacheService object is null, so find from Spring context.");

                setCacheService(getBean(CacheService.class));
                Assert.notNull(cacheService, "CacheService object is still null. Is there no CacheService instance?");
            }

            setInitialized(true);
        }
    }

    /**
     * 返回此缓存使用的缓存服务对象实例
     *
     * @return <code>CacheService</code>
     */
    public final CacheService getCacheService() {
        return cacheService;
    }

    /**
     * 设置此缓存使用到的服务对象实例
     *
     * @param cacheService <code>CacheService</code>对象
     */
    public final void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 返回此类是否已经被初始化过
     *
     * @return 真或假
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * 设置标识当前类是否已经被初始化过
     *
     * @param initialized 布尔值
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
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
