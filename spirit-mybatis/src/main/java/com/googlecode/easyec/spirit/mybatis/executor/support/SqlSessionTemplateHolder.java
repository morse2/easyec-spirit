package com.googlecode.easyec.spirit.mybatis.executor.support;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * <code>SqlSessionTemplate</code>的holder类。
 * 该类在当前事务线程中决定了使用的<code>SqlSessionTemplate</code>
 * 是哪个。
 *
 * @author JunJie
 * @see org.mybatis.spring.SqlSessionTemplate
 */
public final class SqlSessionTemplateHolder {

    private static ThreadLocal<List<SqlSessionTemplateRef>> _t = new ThreadLocal<List<SqlSessionTemplateRef>>();

    private SqlSessionTemplateHolder() {
        // no op
    }

    public static SqlSessionTemplate get() {
        List<SqlSessionTemplateRef> list = _t.get();

        if (isEmpty(list)) return null;

        SqlSessionTemplate sqlSessionTemplate = list.get(0).getSqlSessionTemplate();
        switch (sqlSessionTemplate.getExecutorType()) {
            case BATCH:
                return sqlSessionTemplate;
            default:
                return list.get(list.size() - 1).getSqlSessionTemplate();
        }
    }

    public static void set(Class<?> type, SqlSessionTemplate sqlSessionTemplate) {
        List<SqlSessionTemplateRef> list = _t.get();

        boolean master = false;
        if (isEmpty(list)) {
            list = new ArrayList<SqlSessionTemplateRef>(5);
            master = true;
        }

        list.add(new SqlSessionTemplateRef(master, type, sqlSessionTemplate));
        _t.set(list);
    }

    public static void remove(Class<?> type) {
        List<SqlSessionTemplateRef> list = _t.get();

        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                if (type.equals(list.get(i).getType())) {
                    list.remove(i);
                    break;
                }
            }

            if (list.isEmpty()) _t.remove();
        }
    }

    private static class SqlSessionTemplateRef {

        private boolean master;
        private Class<?> type;
        private SqlSessionTemplate sqlSessionTemplate;

        private SqlSessionTemplateRef(boolean master, Class<?> type, SqlSessionTemplate sqlSessionTemplate) {
            this.master = master;
            this.type = type;
            this.sqlSessionTemplate = sqlSessionTemplate;
        }

        public boolean isMaster() {
            return master;
        }

        public Class<?> getType() {
            return type;
        }

        public SqlSessionTemplate getSqlSessionTemplate() {
            return sqlSessionTemplate;
        }
    }
}
