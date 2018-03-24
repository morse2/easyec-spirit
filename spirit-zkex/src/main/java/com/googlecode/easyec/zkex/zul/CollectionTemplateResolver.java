package com.googlecode.easyec.zkex.zul;

import org.zkoss.zk.ui.util.Template;

public interface CollectionTemplateResolver<T> {

    Template resolve(T paramT);
}
