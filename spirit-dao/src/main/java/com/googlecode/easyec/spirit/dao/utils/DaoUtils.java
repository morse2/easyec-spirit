package com.googlecode.easyec.spirit.dao.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class DaoUtils {

    private DaoUtils() {}

    /**
     * 将参数对象转换为集合类。
     * <ul>
     * <li>如果对象是null，则返回空集合</li>
     * <li>如果对象为集合，则直接返回</li>
     * <li>如果是数组类型，则包装为list</li>
     * <li>如果是map，且包含list的参数key，则将其包装为集合</li>
     * <li>如果是map，且包含array的参数key，则将其包装为集合</li>
     * <li>如果是单对象，则直接包装为集合</li>
     * </ul>
     *
     * @param o <code>java.lang.Object</code>
     * @return <code>java.util.Collection</code>
     */
    public static Collection<?> convertToCollection(Object o) {
        if (null == o) return emptyList();

        if (o instanceof Collection) {
            return (Collection) o;
        }

        if (o.getClass().isArray()) {
            return new ArrayList<>(Arrays.asList((Object[]) o));
        }

        if (o instanceof Map && ((Map) o).containsKey("list")) {
            return (Collection) ((Map) o).get("list");
        }

        if (o instanceof Map && ((Map) o).containsKey("array")) {
            return new ArrayList<>(singletonList(((Map) o).get("array")));
        }

        return new ArrayList<>(singletonList(o));
    }
}
