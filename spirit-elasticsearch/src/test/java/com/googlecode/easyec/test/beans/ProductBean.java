package com.googlecode.easyec.test.beans;

import com.googlecode.easyec.es.ElasticsearchId;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(
    indexName = "my_index",
    type = "product"
)
public class ProductBean implements ElasticsearchId {

    @Field
    private String id;
    @Field
    private String name;

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
}
