package com.googlecode.easyec.zkoss.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.Selectors;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * ZK css选择器扩展工具类
 *
 * @author JunJie
 * @see Selectors
 */
public class SelectorUtils {

    private static final Logger logger = LoggerFactory.getLogger(SelectorUtils.class);

    private SelectorUtils() { /* no op */ }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param root     根组件对象，搜索的起始范围
     * @param selector css选择器表达式
     * @return 符合条件的第一个组件对象
     */
    public static Component findFirst(Component root, String selector) {
        return findFirst(root, selector, true);
    }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param root       根组件对象，搜索的起始范围
     * @param selector   css选择器表达式
     * @param findInPage 指出如果给定的组件级别范围不能找到相应的组件，则是否要从Page范围进行查找
     * @return 符合条件的第一个组件对象
     */
    public static Component findFirst(Component root, String selector, boolean findInPage) {
        List<Component> list = find(root, selector, findInPage);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param root     根组件对象，搜索的起始范围
     * @param selector css选择器表达式
     * @return 符合条件的组件对象集合
     */
    public static List<Component> find(Component root, String selector) {
        return find(root, selector, true);
    }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param root       根组件对象，搜索的起始范围
     * @param selector   css选择器表达式
     * @param findInPage 指出如果给定的组件级别范围不能找到相应的组件，则是否要从Page范围进行查找
     * @return 符合条件的组件对象集合
     */
    public static List<Component> find(Component root, String selector, boolean findInPage) {
        if (null == root) {
            logger.debug("Component is null.");

            return emptyList();
        }

        List<Component> result = Selectors.find(root, selector);
        return isEmpty(result) && findInPage ? find(root.getPage(), selector) : result;
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param root     根组件对象，搜索的起始范围
     * @param selector css选择器表达式
     * @param clazz    指定查找的组件的类型
     * @param <T>      组件的泛型类型
     * @return 符合条件的第一个组件对象
     */
    public static <T extends Component> T findFirst(Component root, String selector, Class<T> clazz) {
        return findFirst(root, selector, clazz, true);
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param root       根组件对象，搜索的起始范围
     * @param selector   css选择器表达式
     * @param clazz      指定查找的组件的类型
     * @param findInPage 指出如果给定的组件级别范围不能找到相应的组件，则是否要从Page范围进行查找
     * @param <T>        组件的泛型类型
     * @return 符合条件的第一个组件对象
     */
    public static <T extends Component> T findFirst(Component root, String selector, Class<T> clazz, boolean findInPage) {
        List<T> list = find(root, selector, clazz, findInPage);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param root     根组件对象，搜索的起始范围
     * @param selector css选择器表达式
     * @param clazz    指定查找的组件的类型
     * @param <T>      组件的泛型类型
     * @return 符合条件的组件对象集合
     */
    public static <T extends Component> List<T> find(Component root, String selector, Class<T> clazz) {
        return find(root, selector, clazz, true);
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param root       根组件对象，搜索的起始范围
     * @param selector   css选择器表达式
     * @param clazz      指定查找的组件的类型
     * @param findInPage 指出如果给定的组件级别范围不能找到相应的组件，则是否要从Page范围进行查找
     * @param <T>        组件的泛型类型
     * @return 符合条件的组件对象集合
     */
    public static <T extends Component> List<T> find(Component root, String selector, Class<T> clazz, boolean findInPage) {
        Assert.notNull(clazz, "Type of class is null.");

        List<T> result = new ArrayList<T>();
        List<Component> list = find(root, selector, findInPage);
        for (Component comp : list) {
            if (clazz.isAssignableFrom(comp.getClass())) {
                result.add(clazz.cast(comp));
            }
        }

        return result;
    }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param page     页面组件对象
     * @param selector css选择器表达式
     * @return 符合条件的第一个组件对象
     */
    public static Component findFirst(Page page, String selector) {
        List<Component> list = find(page, selector);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查找给定的css选择器的组件信息。
     *
     * @param page     页面组件对象
     * @param selector css选择器表达式
     * @return 符合条件的组件对象集合
     */
    public static List<Component> find(Page page, String selector) {
        if (null == page) {
            logger.debug("Page object of Component is null.");

            return emptyList();
        }

        return Selectors.find(page, selector);
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param page     页面组件对象
     * @param selector css选择器表达式
     * @param clazz    指定查找的组件的类型
     * @param <T>      组件的泛型类型
     * @return 符合条件的第一个组件对象
     */
    public static <T extends Component> T findFirst(Page page, String selector, Class<T> clazz) {
        List<T> list = find(page, selector, clazz);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查找给定的css选择器的组件信息。
     * 并且组件类型是给定的类型，
     * 不符合给定的类型的组件将被忽略过滤。
     *
     * @param page     页面组件对象
     * @param selector css选择器表达式
     * @param clazz    指定查找的组件的类型
     * @param <T>      组件的泛型类型
     * @return 符合条件的组件对象集合
     */
    public static <T extends Component> List<T> find(Page page, String selector, Class<T> clazz) {
        Assert.notNull(clazz, "Type of class is null.");

        List<T> result = new ArrayList<T>();
        List<Component> list = find(page, selector);
        for (Component comp : list) {
            if (clazz.isAssignableFrom(comp.getClass())) {
                result.add(clazz.cast(comp));
            }
        }

        return result;
    }
}
