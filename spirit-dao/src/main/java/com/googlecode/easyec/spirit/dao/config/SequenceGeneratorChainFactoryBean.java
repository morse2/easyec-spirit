package com.googlecode.easyec.spirit.dao.config;

import com.googlecode.easyec.spirit.dao.id.IdentifierGenerator;
import com.googlecode.easyec.spirit.dao.id.SequenceGenerator;
import com.googlecode.easyec.spirit.dao.id.impl.*;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 序列生成器链对象工厂类
 *
 * @author JunJie
 */
public class SequenceGeneratorChainFactoryBean extends AbstractFactoryBean<DomainModelSequenceGeneratorChain> {

    // ----- local variables definition here
    private DataSource dataSource;
    private boolean useDefault;
    private String converter;
    private int maxLoVal;

    private List<IdentifierGenerator> customIdentifierGenerators;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public void setMaxLoVal(int maxLoVal) {
        this.maxLoVal = maxLoVal;
    }

    public void setCustomIdentifierGenerators(List<IdentifierGenerator> sequenceGenerators) {
        this.customIdentifierGenerators = sequenceGenerators;
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

        if (useDefault) {
            // 初始化默认的IdentifierGenerator对象实例
            chain.addIdentifierGenerator(new LongValueHiloIdentifierGenerator(maxLoVal));
            chain.addIdentifierGenerator(new ShortValueHiloIdentifierGenerator(maxLoVal));
            chain.addIdentifierGenerator(new IntegerValueHiloIdentifierGenerator(maxLoVal));
        }

        if (!isEmpty(customIdentifierGenerators)) {
            for (IdentifierGenerator ig : customIdentifierGenerators) {
                chain.addIdentifierGenerator(ig);
            }
        }

        if (isEmpty(chain.getIdentifierGenerators())) {
            throw new IllegalArgumentException("There is no any 'IdentifierGenerator' set.");
        }

        return chain;
    }
}
