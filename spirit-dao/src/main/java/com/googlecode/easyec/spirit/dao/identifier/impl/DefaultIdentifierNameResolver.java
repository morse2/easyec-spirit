package com.googlecode.easyec.spirit.dao.identifier.impl;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierNameResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * 默认的主键名称解析器类。
 * 此类是根据PO对象的实例，来获取此PO对象的类名，
 * 然后根据前缀和后缀的拼接，来获取此类对应的主键名称。
 *
 * @author JunJie
 * @see AnnotationIdentifierNameResolver
 */
public class DefaultIdentifierNameResolver implements IdentifierNameResolver {

    private String prefix;
    private String suffix;

    public String populate(Object o) {
        return getIdName(o);
    }

    /**
     * 设置主键名称的前缀
     *
     * @param prefix 前缀名
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 设置主键名称的后缀
     *
     * @param suffix 后缀名
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 返回一个关于当前持久化对象的主键名。
     *
     * @param o 持久化对象
     * @return 主键名字
     */
    private String getIdName(Object o) {
        StringBuffer sb = new StringBuffer();

        if (StringUtils.isNotBlank(prefix)) sb.append(prefix);

        if (o instanceof Map) {
            Map map = (Map) o;

            if (map.containsKey("list")) {
                sb.append(getObjectName((Collection) map.get("list")));
            } else if (map.containsKey("array")) {
                sb.append(getObjectName(Arrays.asList(map.get("array"))));
            }
        } else if (o instanceof Collection) {
            sb.append(getObjectName((Collection) o));
        } else if (o.getClass().isArray()) {
            sb.append(getObjectName(Arrays.asList((Object[]) o)));
        } else {
            sb.append(o.getClass().getSimpleName());
        }

        if (StringUtils.isNotBlank(suffix)) sb.append(suffix);

        return sb.toString();
    }

    private String getObjectName(Collection c) {
        Assert.isTrue(isNotEmpty(c), "Parameter object has no any value.");

        Iterator iter = c.iterator();

        return iter.hasNext() ? iter.next().getClass().getSimpleName() : "";
    }
}
