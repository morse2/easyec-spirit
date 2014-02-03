package com.googlecode.easyec.spirit.dao.config;

import com.googlecode.easyec.spirit.dao.id.SequenceGenerator;
import com.googlecode.easyec.spirit.dao.id.impl.*;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;

/**
 * 序列生成器链对象工厂类
 *
 * @author JunJie
 */
public class SequenceGeneratorChainFactoryBean extends AbstractFactoryBean<DomainModelSequenceGeneratorChain> {

    // ----- local variables definition here
    private DataSource dataSource;
    private String     converter;
    private int        maxLoVal;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public void setMaxLoVal(int maxLoVal) {
        this.maxLoVal = maxLoVal;
    }

    @Override
    public Class<?> getObjectType() {
        return SequenceGenerator.class;
    }

    @Override
    protected DomainModelSequenceGeneratorChain createInstance() throws Exception {
        DomainModelSequenceGeneratorChain chain = new DomainModelSequenceGeneratorChain();

        chain.setDataSource(dataSource);

        if ("annotation".equals(converter)) {
            chain.setIdentifierNameConverter(new AnnotatedIdentifierNameConverter());
        }

        // 初始化默认的IdentifierGenerator对象实例
        chain.addIdentifierGenerator(new LongValueHiloIdentifierGenerator(maxLoVal));
        chain.addIdentifierGenerator(new ShortValueHiloIdentifierGenerator(maxLoVal));
        chain.addIdentifierGenerator(new IntegerValueHiloIdentifierGenerator(maxLoVal));

        return chain;
    }
}
