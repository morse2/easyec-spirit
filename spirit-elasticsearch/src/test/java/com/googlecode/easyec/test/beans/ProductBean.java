package com.googlecode.easyec.test.beans;

import com.googlecode.easyec.es.ElasticsearchId;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.ArrayList;
import java.util.List;

@Document(
    indexName = "my_index",
    type = "product"
)
public class ProductBean implements ElasticsearchId {

    @Field
    private String id;
    @Field
    private String name;
    @Field
    private List<AttributeBean> attrs = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUidPk() {
        return getId();
    }

    @Override
    public void setUidPk(String uidPk) {
        setId(uidPk);
    }

    public List<AttributeBean> getAttrs() {
        return attrs;
    }

    public void addAttr(AttributeBean bean) {
        this.attrs.add(bean);
    }
}
