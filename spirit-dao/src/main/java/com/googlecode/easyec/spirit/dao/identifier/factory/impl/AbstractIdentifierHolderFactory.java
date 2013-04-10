package com.googlecode.easyec.spirit.dao.identifier.factory.impl;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierHolder;
import com.googlecode.easyec.spirit.dao.identifier.IdentifierNameResolver;
import com.googlecode.easyec.spirit.dao.identifier.factory.IdentifierHolderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-11-12
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractIdentifierHolderFactory implements IdentifierHolderFactory, InitializingBean {

    private final ConcurrentMap<String, IdentifierHolder> identifierHolderMap;
    private IdentifierNameResolver identifierNameResolver;

    protected AbstractIdentifierHolderFactory() {
        identifierHolderMap = new ConcurrentHashMap<String, IdentifierHolder>(50);
    }

    public void setIdentifierNameResolver(IdentifierNameResolver identifierNameResolver) {
        this.identifierNameResolver = identifierNameResolver;
    }

    public IdentifierHolder find(String id) {
        return identifierHolderMap.get(id);
    }

    public boolean put(String id, IdentifierHolder holder) {
        return identifierHolderMap.putIfAbsent(id, holder) != null;
    }

    public boolean remove(String id) {
        return identifierHolderMap.remove(id) != null;
    }

    public void generate(Object o) throws Exception {
        if (o != null) generate(identifierNameResolver, o);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(identifierNameResolver, "IdentifierNameResolver object cannot be null.");
    }

    abstract public void generate(IdentifierNameResolver identifierNameResolver, Object o) throws Exception;
}
