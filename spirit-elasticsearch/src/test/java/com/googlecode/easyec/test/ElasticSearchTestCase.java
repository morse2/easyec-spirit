package com.googlecode.easyec.test;

import com.googlecode.easyec.es.formbean.ElasticsearchFormBean;
import com.googlecode.easyec.es.paging.EsPage;
import com.googlecode.easyec.es.service.ElasticsearchService;
import com.googlecode.easyec.test.beans.ProductBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import javax.annotation.Resource;
import java.util.Arrays;

@RunWith(SpringRunner.class)
//@WebAppConfiguration
@ContextConfiguration(locations = "classpath:spring/test/applicationContext-*.xml")
@TestExecutionListeners(listeners = {
    ServletTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class
})
public class ElasticSearchTestCase {

    @Resource
    private ElasticsearchService elasticsearchService;

    @BeforeClass
    public static void beforeClass() {
    }

    @Test
    public void saveProd() throws Exception {
        ProductBean prod = new ProductBean();
        prod.setId("1234567");
        prod.setName("This is web camera 3.");

        elasticsearchService.save(
            Arrays.asList(prod),
            ProductBean.class
        );
    }

    @Test
    public void deleteProd() throws Exception {
        ProductBean bean = new ProductBean();
        bean.setUidPk("12345");

        elasticsearchService.delete(bean);
    }

    @Test
    public void getOne() throws Exception {
        new NativeSearchQueryBuilder();

        ProductBean bean = elasticsearchService.get("1234", ProductBean.class);
        Assert.assertNotNull(bean);
    }

    @Test
    public void findPaged() throws Exception {
        ElasticsearchFormBean<ProductBean> bean = new ElasticsearchFormBean<>(ProductBean.class);
        bean.addIndices("my_index");
        bean.addTypes("product");
        bean.addSearchTerm("name", "web");

        EsPage<ProductBean> page = elasticsearchService.findPaged(bean, 10);
        Assert.assertNotNull(page);
        Assert.assertFalse(page.getNextPageAvailable());
    }
}
