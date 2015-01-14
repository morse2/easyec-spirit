package com.googlecode.easyec.spirit.ldap.template.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;

import javax.annotation.Resource;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.ModificationItem;
import java.util.ArrayList;
import java.util.List;

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

    private ModificationItem _createModificationItem(String id, Object value, int modifyOp) {
        return new ModificationItem(
            modifyOp, new BasicAttribute(id, value)
        );
    }
}
