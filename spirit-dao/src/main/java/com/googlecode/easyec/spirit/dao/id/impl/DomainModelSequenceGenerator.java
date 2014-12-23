package com.googlecode.easyec.spirit.dao.id.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierNameConverter;
import com.googlecode.easyec.spirit.dao.id.SequenceGenerator;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

/**
 * 通用域模型的主键生成器实现类。
 * <p>
 * 该类主要截获类<code>GenericPersistentDomainModel</code>
 * 对象及其子类对象，并根据条件判断是否需要为其自动生成主键值
 * </p>
 * <p>
 * 生成主键值的规则为：
 * <ol>
 * <li>域模型中的uidPk值为空</li>
 * <li>域模型中的uidPk值如果为数字类型，则值是否小于或等于0</li>
 * <li>域模型中的uidPk值类型不可以为接口<code>Serializable</code></li>
 * </ol>
 * </p>
 *
 * @author JunJie
 */
public abstract class DomainModelSequenceGenerator implements SequenceGenerator, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected IdentifierNameConverter converter = new ClassIdentifierNameConverter();

    public void setIdentifierNameConverter(IdentifierNameConverter converter) {
        this.converter = converter;
    }

    @SuppressWarnings("unchecked")
    public final synchronized void process(Object arg) throws Exception {
        Collection args = resolveArg(arg);

        for (Object o : args) {
            if (null == o) {
                logger.warn("Parameter arg is null, so ignore it.");

                continue;
            }

            if (!GenericPersistentDomainModel.class.isInstance(o)) {
                logger.warn("Parameter arg isn't instance of GenericPersistentDomainModel object.");

                continue;
            }

            BeanWrapper bw = forBeanPropertyAccess(o);
            PropertyDescriptor pd = bw.getPropertyDescriptor("uidPk");
            Class<?> type = pd.getWriteMethod().getParameterTypes()[0];
            logger.debug("GenericPersistentDomainModel's parameter type of write method 'uidPk' is: [{}].", type.getName());

            if (Serializable.class.getName().equals(type.getName())) {
                logger.warn("Parameter type of method 'uidPk' is: [{}], so ignore.", Serializable.class.getName());

                continue;
            }

            if (Number.class.isAssignableFrom(type)) {
                Number uidPk = (Number) bw.getPropertyValue("uidPk");
                if (null != uidPk && uidPk.longValue() > 0) {
                    logger.warn("GenericPersistentDomainModel has primary key value, so ignore. Original uid: [{}].", uidPk);

                    continue;
                }
            }

            logger.debug("Do processDomainModel method.");
            processDomainModel((GenericPersistentDomainModel<Serializable>) o, type);
        }
    }

    /**
     * 处理域模型对象的主键的方法。
     *
     * @param domainModel   域模型对象
     * @param parameterType 域模型对象中主键参数类型
     * @throws Exception
     */
    abstract public void processDomainModel(GenericPersistentDomainModel<Serializable> domainModel, Class<?> parameterType) throws Exception;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(converter, "Identifier name converter object cannot be null.");
    }

    /**
     * 解析参数对象并返回一个集合对象。
     *
     * @param o 参数对象
     * @return <code>Collection</code>
     */
    private Collection resolveArg(Object o) {
        if (null == o) return emptyList();

        if (o instanceof Collection) {
            return (Collection) o;
        }

        if (o.getClass().isArray()) {
            return new ArrayList<Object>(Arrays.asList((Object[]) o));
        }

        if (o instanceof Map && ((Map) o).containsKey("list")) {
            return (Collection) ((Map) o).get("list");
        }

        if (o instanceof Map && ((Map) o).containsKey("array")) {
            return new ArrayList<Object>(Arrays.asList(((Map) o).get("array")));
        }

        return new ArrayList<Object>(Arrays.asList(o));
    }
}
