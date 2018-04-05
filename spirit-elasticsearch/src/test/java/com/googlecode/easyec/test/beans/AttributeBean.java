package com.googlecode.easyec.test.beans;

import org.springframework.data.elasticsearch.annotations.Field;

import java.util.HashMap;
import java.util.Map;

public class AttributeBean {

    @Field
    private String code;
    @Field
    private Map<String, String> labels = new HashMap<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void addLabel(String locale, String value) {
        this.labels.put(locale, value);
    }
}
