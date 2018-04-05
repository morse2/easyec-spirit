package com.googlecode.easyec.es.formbean;

import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

public class ElasticsearchFormBean<T> extends SearchFormBean {

    private static final long serialVersionUID = 5463143729179683552L;
    private Set<String> indices = new HashSet<>();
    private Set<String> types = new HashSet<>();
    private Class<T> cls;

    public ElasticsearchFormBean(Class<T> cls) {
        Assert.notNull(cls, "Entity class mustn't be null.");
        this.cls = cls;
    }

    public void addIndices(String... indices) {
        if (isNotEmpty(indices)) {
            this.indices.addAll(Arrays.asList(indices));
        }
    }

    public void addTypes(String... types) {
        if (isNotEmpty(types)) {
            this.types.addAll(Arrays.asList(types));
        }
    }

    public Set<String> getIndices() {
        return indices;
    }

    public Set<String> getTypes() {
        return types;
    }

    public Class<T> getEntityClass() {
        return cls;
    }
}
