package com.googlecode.easyec.spirit.ldap.template.support;

import org.springframework.util.Assert;

import static com.googlecode.easyec.spirit.ldap.template.support.LdapItemOperation.LdapItemOperationType.MODIFY_ITEM;
import static com.googlecode.easyec.spirit.ldap.template.support.LdapItemOperation.LdapItemOperationType.REMOVE_ITEM;

/**
 * LDAP内容项的操作类
 *
 * @author JunJie
 */
public class LdapItemOperation {

    /**
     * LDAP内容项的操作枚举类
     */
    public enum LdapItemOperationType {

        /**
         * 添加内容
         */
        ADD_ITEM(1),
        /**
         * 修改内容
         */
        MODIFY_ITEM(2),
        /**
         * 删除内容
         */
        REMOVE_ITEM(3);

        private int value;

        LdapItemOperationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String id;
    private Object value;
    private LdapItemOperationType type;

    public LdapItemOperation(String id) {
        this(id, null);
    }

    public LdapItemOperation(String id, Object value) {
        this(id, value, value == null ? REMOVE_ITEM : MODIFY_ITEM);
    }

    public LdapItemOperation(String id, Object value, LdapItemOperationType type) {
        Assert.notNull(id, "Id cannot be null.");
        Assert.notNull(type, "LdapItemOperationType cannot be null.");

        this.id = id;
        this.value = value;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    public LdapItemOperationType getType() {
        return type;
    }
}
