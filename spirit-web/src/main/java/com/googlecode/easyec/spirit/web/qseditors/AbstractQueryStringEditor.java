package com.googlecode.easyec.spirit.web.qseditors;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractQueryStringEditor implements QueryStringEditor {

    private static final long serialVersionUID = -8043971237991642613L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean accept(Object bean) {
        return bean != null;
    }

    @Override
    public String[] coerceToQs(Object bean) {
        if (bean instanceof Collection) {
            return ((Collection<?>) bean).stream()
                .map(this::coerceOneObjectToQs)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        }

        return Stream.of(bean)
            .map(this::coerceOneObjectToQs)
            .filter(Objects::nonNull)
            .toArray(String[]::new);
    }

    @Override
    public Object coerceToBean(String[] qs) {
        if (ArrayUtils.isEmpty(qs)) return null;

        List<Object> result
            = Stream.of(qs)
            .map(this::coerceOneValueToBean)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return !result.isEmpty()
            ? result.size() > 1
            ? result : result.get(0)
            : null;
    }

    /**
     * 将特定的一个Bean转换成字符串值。
     * 如果返回的字符串是null或是空，
     * 那么该字符串则不会被输出。
     *
     * @param bean <code>java.lang.Object</code>
     * @return <code>java.lang.String</code>
     */
    abstract protected String coerceOneObjectToQs(Object bean);

    /**
     * 将一个字符串值转换为对象Bean。
     * 如果返回的Bean是null，那么
     * 该Bean则不被接受。
     *
     * @param qs <code>java.lang.String</code>
     * @return <code>java.lang.Object</code>
     */
    abstract protected Object coerceOneValueToBean(String qs);
}
