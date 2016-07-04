package com.googlecode.easyec.zkoss.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Tree控件工具类。
 *
 * @author JunJie
 */
public class TreeUtils {

    /**
     * 获取此<code>Treeitem</code>对象中，
     * 第一个<code>Treecell</code>的标签名。
     *
     * @param item ZK UI <code>Treeitem</code>
     * @return <code>Treecell</code>的标签名字
     */
    public static String getFirstTreecellLabel(Treeitem item) {
        assertTreeitem(item);

        List<Component> children = item.getTreerow().getChildren();

        assertTreecell(children);

        return ((Treecell) children.get(0)).getLabel();
    }

    /**
     * 获取此<code>Treeitem</code>对象中，
     * 第一个<code>Treecell</code>中存在的键对应的值。
     *
     * @param item ZK UI <code>Treeitem</code>
     * @param key  键名字
     * @return 键对应的值
     */
    public static Object getFirstTreecellAttribute(Treeitem item, String key) {
        assertTreeitem(item);

        List<Component> children = item.getTreerow().getChildren();

        assertTreecell(children);

        return children.get(0).getAttribute(key);
    }

    /**
     * 查找此<code>Treeitem</code>对象在整个
     * <code>Tree</code>中的节点。
     *
     * @param item ZK UI <code>Treeitem</code>
     * @return 节点信息，数组表示
     */
    public static int[] findTreeitemPath(Treeitem item) {
        if (null == item) return new int[0];

        int[] i = { item.getIndex() };

        return ArrayUtils.addAll(findTreeitemPath(item.getParentItem()), i);
    }

    /**
     * 断言<code>Treeitem</code>对象是否为空
     *
     * @param item ZK UI <code>Treeitem</code>
     */
    private static void assertTreeitem(Treeitem item) {
        Assert.notNull(item, "Treeitem object is null.");
    }

    /**
     * 断言<code>Treecell</code>列表是否为空
     *
     * @param treecells ZK UI <code>Treecell</code>对象列表
     */
    private static void assertTreecell(List<Component> treecells) {
        Assert.isTrue(isNotEmpty(treecells), "Treerow object hasn't any children nodes.");
    }
}
