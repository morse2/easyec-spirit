package com.googlecode.easyec.spirit.ldap.template.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;

import javax.annotation.Resource;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.ModificationItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.googlecode.easyec.spirit.ldap.template.support.LdapItemOperation.LdapItemOperationType.ADD_ITEM;
import static com.googlecode.easyec.spirit.ldap.template.support.LdapItemOperation.LdapItemOperationType.REMOVE_ITEM;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEquals;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * LDAP模板操作的支持类
 *
 * @author JunJie
 */
public abstract class LdapTemplateSupport {

    /* logger object */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    protected LdapTemplate ldapTemplate;

    protected LdapTemplateSupport() { /* no op */ }

    /**
     * 创建一组修改内容
     *
     * @param operations LDAP内容操作对象
     */
    protected ModificationItem[] createModificationItems(List<LdapItemOperation> operations) {
        List<ModificationItem> list = new ArrayList<ModificationItem>();
        for (LdapItemOperation operation : operations) {
            list.add(
                createModificationItem(operation)
            );
        }

        return list.toArray(new ModificationItem[list.size()]);
    }

    /**
     * 创建一个修改内容
     *
     * @param operation LDAP内容的操作类型
     */
    protected ModificationItem createModificationItem(LdapItemOperation operation) {
        return _createModificationItem(operation.getId(), operation.getValue(), operation.getType().getValue());
    }

    /**
     * 依据给定的参数3和参数4的值，进行比较操作，
     * 来创建一个<code>LdapItemOperation</code>对象，
     * 并将此对象放入参数1的集合对象中。
     *
     * @param coll   LdapItemOperation对象集合
     * @param key    属性名称
     * @param oldVal 旧值（在LDAP系统中已存在）
     * @param newVal 新值（用户给到）
     */
    protected void createLdapItemOperation(Collection<LdapItemOperation> coll, String key, String oldVal, String newVal) {
        if (isBlank(oldVal) && isBlank(newVal)) return;
        if (isBlank(oldVal)) coll.add(new LdapItemOperation(key, newVal, ADD_ITEM));
        else if (isBlank(newVal)) coll.add(new LdapItemOperation(key, oldVal, REMOVE_ITEM));
        else if (!oldVal.equals(newVal)) coll.add(new LdapItemOperation(key, newVal));
    }

    /**
     * 依据给定的参数3和参数4的值，进行比较操作，
     * 来创建一个<code>LdapItemOperation</code>对象，
     * 并将此对象放入参数1的集合对象中。
     *
     * @param coll   LdapItemOperation对象集合
     * @param key    属性名称
     * @param oldVal 旧值（在LDAP系统中已存在）
     * @param newVal 新值（用户给到）
     */
    protected void createLdapItemOperation(Collection<LdapItemOperation> coll, String key, byte[] oldVal, byte[] newVal) {
        if (isEmpty(oldVal) && isEmpty(newVal)) return;
        if (isEmpty(oldVal)) coll.add(new LdapItemOperation(key, newVal, ADD_ITEM));
        else if (isEmpty(newVal)) coll.add(new LdapItemOperation(key, oldVal, REMOVE_ITEM));
        else if (!isEquals(oldVal, newVal)) coll.add(new LdapItemOperation(key, newVal));
    }

    /**
     * 依据给定的参数3和参数4的值，进行比较操作，
     * 来创建一个<code>LdapItemOperation</code>对象，
     * 并将此对象放入参数1的集合对象中。
     *
     * @param coll   LdapItemOperation对象集合
     * @param key    属性名称
     * @param oldVal 旧值（在LDAP系统中已存在）
     * @param newVal 新值（用户给到）
     */
    protected void createLdapItemOperation(Collection<LdapItemOperation> coll, String key, Object oldVal, Object newVal) {
        if (oldVal == null && newVal == null) return;
        if (oldVal == null) coll.add(new LdapItemOperation(key, newVal, ADD_ITEM));
        else if (newVal == null) coll.add(new LdapItemOperation(key, oldVal, REMOVE_ITEM));
        else if (!oldVal.equals(newVal)) coll.add(new LdapItemOperation(key, newVal));
    }

    private ModificationItem _createModificationItem(String id, Object value, int modifyOp) {
        return new ModificationItem(
            modifyOp, new BasicAttribute(id, value)
        );
    }
}
