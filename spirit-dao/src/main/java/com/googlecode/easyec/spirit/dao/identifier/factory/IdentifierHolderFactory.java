package com.googlecode.easyec.spirit.dao.identifier.factory;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-11-12
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public interface IdentifierHolderFactory {

    IdentifierHolder find(String id);

    boolean put(String id, IdentifierHolder holder);

    boolean remove(String id);

    void generate(Object o) throws Exception;
}
