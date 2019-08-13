package com.googlecode.easyec.spirit.dao.id.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierGenerator;
import com.googlecode.easyec.spirit.dao.id.IgnoreIdentifierGenerateException;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.jdbc.datasource.DataSourceUtils.getConnection;
import static org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection;

/**
 * 域模型对象的主键值序列化生成器过滤链类
 *
 * @author JunJie
 */
public class DomainModelSequenceGeneratorChain extends DomainModelSequenceGenerator {

    private final List<IdentifierGenerator> generators = new ArrayList<IdentifierGenerator>();

    private DataSource dataSource;

    /**
     * 设置一个数据源实例
     *
     * @param dataSource 数据源对象
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 设置一组主键生成器对象实例
     *
     * @param generators 主键生成器类对象列表
     */
    public void setIdentifierGenerators(List<IdentifierGenerator> generators) {
        if (isNotEmpty(generators)) this.generators.addAll(generators);
    }

    /**
     * 添加一个主键生成器对象实例
     *
     * @param generator 主键生成器类对象
     */
    public boolean addIdentifierGenerator(IdentifierGenerator generator) {
        if (null == generator) return false;
        this.generators.add(generator);
        return true;
    }

    /**
     * 返回当前不可变的主键生成器对象列表
     *
     * @return 主键生成器类对象列表
     */
    public List<IdentifierGenerator> getIdentifierGenerators() {
        return unmodifiableList(this.generators);
    }

    public void processDomainModel(GenericPersistentDomainModel<Serializable> domainModel, Class<?> parameterType) throws Exception {
        if (isEmpty(generators)) {
            logger.warn("No any IdentifierGenerator was set.");

            return;
        }

        Connection conn = null;

        try {
            String name = getConverter().populate(domainModel);
            logger.debug("Identifier name: [{}].", name);

            conn = getConnection(dataSource);

            boolean ret = false;
            for (IdentifierGenerator generator : generators) {
                if (generator.accept(parameterType)) {
                    Serializable ser = generator.generate(name, conn);
                    if (null != ser) {
                        domainModel.setUidPk(ser);
                        ret = true;

                        break;
                    }
                }
            }

            if (!ret) logger.warn("No any new sequence key was generated.");
        } catch (IgnoreIdentifierGenerateException e) {
            logger.trace(e.getMessage(), e);
        } finally {
            releaseConnection(conn, dataSource);
        }
    }
}
