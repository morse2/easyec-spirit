package com.googlecode.easyec.es.service;

import com.googlecode.easyec.es.ElasticsearchId;
import com.googlecode.easyec.es.formbean.ElasticsearchFormBean;
import com.googlecode.easyec.es.paging.EsPage;
import com.googlecode.easyec.es.paging.support.EsPageImpl;
import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.elasticsearch.common.xcontent.XContentType.JSON;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class ElasticsearchService extends ElasticsearchTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    public ElasticsearchService(Client client) {
        super(client);
    }

    public ElasticsearchService(Client client, EntityMapper entityMapper) {
        super(client, entityMapper);
    }

    public ElasticsearchService(Client client, ElasticsearchConverter elasticsearchConverter, EntityMapper entityMapper) {
        super(client, elasticsearchConverter, entityMapper);
    }

    public ElasticsearchService(Client client, ResultsMapper resultsMapper) {
        super(client, resultsMapper);
    }

    public ElasticsearchService(Client client, ElasticsearchConverter elasticsearchConverter) {
        super(client, elasticsearchConverter);
    }

    public ElasticsearchService(Client client, ElasticsearchConverter elasticsearchConverter, ResultsMapper resultsMapper) {
        super(client, elasticsearchConverter, resultsMapper);
    }

    public <T extends ElasticsearchId> void delete(T id) {
        Assert.notNull(id.getUidPk(), "ID value cannot be null.");

        String s = delete(id.getClass(), id.getUidPk());
        logger.info("ID: [{}] has been deleted from index.", s);
    }

    public <T extends ElasticsearchId>
    void save(List<T> list, Class<T> cls) throws DataPersistenceException {
        if (CollectionUtils.isNotEmpty(list)) {
            List<UpdateQuery> queries = new ArrayList<>(list.size());

            try {
                for (ElasticsearchId _val : list) {
                    String _str
                        = getResultsMapper()
                        .getEntityMapper()
                        .mapToString(_val);
                    logger.info("Source string for class [{}] is: [{}]", cls, _str);

                    queries.add(
                        new UpdateQueryBuilder()
                            .withId(_val.getUidPk())
                            .withClass(cls)
                            .withIndexRequest(
                                new IndexRequest()
                                    .source(_str, JSON)
                            ).withDoUpsert(true)
                            .build()
                    );
                }

                bulkUpdate(queries);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new DataPersistenceException(e);
            } finally {
                queries.clear();
                queries = null;
            }
        }
    }

    public <T> T get(String id, Class<T> cls) {
        GetQuery query = new GetQuery();
        query.setId(id);

        return queryForObject(query, cls);
    }

    public <T> List<T> find(ElasticsearchFormBean<T> bean) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        bean.getIndices().forEach(builder::withIndices);
        bean.getTypes().forEach(builder::withTypes);

        Map<String, Object> terms = bean.getSearchTerms();
        if (isNotEmpty(terms)) {
            BoolQueryBuilder bqBuilder = new BoolQueryBuilder();
            terms.forEach((k, v) -> bqBuilder.filter(termQuery(k, v)));
            builder.withQuery(bqBuilder);

            // TODO: 2018/4/4 build terms for other situation
        }

        return queryForList(
            builder.build(),
            bean.getEntityClass()
        );
    }

    public <T> EsPage<T> findPaged(ElasticsearchFormBean<T> bean, int pageSize) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        bean.getIndices().forEach(builder::withIndices);
        bean.getTypes().forEach(builder::withTypes);

        builder.withPageable(
            PageRequest.of(bean.getPageNumber() - 1, pageSize)
        );
        // TODO: 2018/4/4 order by here

        Map<String, Object> terms = bean.getSearchTerms();
        if (isNotEmpty(terms)) {
            BoolQueryBuilder bqBuilder = new BoolQueryBuilder();
            terms.forEach((k, v) -> bqBuilder.filter(termQuery(k, v)));
            builder.withQuery(bqBuilder);

            // TODO: 2018/4/4 build terms for other situation
        }

        AggregatedPage<T> _page
            = queryForPage(
            builder.build(),
            bean.getEntityClass()
        );

        EsPageImpl<T> page = new EsPageImpl<>(
            bean.getPageNumber(),
            pageSize,
            _page.getTotalElements()
        );

        page.setRecords(_page.getContent());

        return page;
    }
}
