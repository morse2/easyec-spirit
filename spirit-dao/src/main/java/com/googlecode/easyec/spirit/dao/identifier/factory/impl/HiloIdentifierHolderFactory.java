package com.googlecode.easyec.spirit.dao.identifier.factory.impl;

import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import com.googlecode.easyec.spirit.dao.identifier.IdentifierHolder;
import com.googlecode.easyec.spirit.dao.identifier.IdentifierNameResolver;
import com.googlecode.easyec.spirit.dao.identifier.factory.IdentifierHolderFactory;
import com.googlecode.easyec.spirit.dao.identifier.impl.HiloIdentifierHolder;
import com.googlecode.easyec.spirit.domain.PersistentDomainModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static com.googlecode.easyec.spirit.dao.identifier.IdentifierWeavingInterceptor.IsolatedWork;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.springframework.jdbc.datasource.DataSourceUtils.getConnection;
import static org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection;
import static org.springframework.util.ClassUtils.getQualifiedName;

/**
 * HILO算法的主键标识句柄工厂类。
 * <p>该类使用了HILO算法来计算表主键的值</p>
 *
 * @author JunJie
 */
public class HiloIdentifierHolderFactory extends AbstractIdentifierHolderFactory implements FactoryBean<IdentifierHolderFactory> {

    private static final Logger logger = LoggerFactory.getLogger(HiloIdentifierHolderFactory.class);
    public static final String DEFAULT_INSERT_SQL = "insert into sequence_generator (id_name, id_value) values (?, ?)";
    public static final String DEFAULT_UPDATE_SQL = "update sequence_generator set id_value = ? where id_name = ?";
    public static final String DEFAULT_SELECT_SQL = "select id_value from sequence_generator where id_name = ?";

    private String insertSql = DEFAULT_INSERT_SQL;
    private String updateSql = DEFAULT_UPDATE_SQL;
    private String selectSql = DEFAULT_SELECT_SQL;

    private DataSource dataSource;
    private long maxLoVal;

    public void setSelectSql(String sql) {
        Assert.isTrue(isNotBlank(sql), "Select SQL mustn't be null.");

        this.selectSql = sql;
    }

    public void setUpdateSql(String sql) {
        this.updateSql = sql;
    }

    public void setInsertSql(String sql) {
        this.insertSql = sql;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxLoVal(long maxLoVal) {
        this.maxLoVal = maxLoVal;
    }

    @Override
    public synchronized void generate(final IdentifierNameResolver identifierNameResolver, Object o) throws Exception {
        // 第一步，将对象解析成集合类
        final Collection c = convertToCollection(o);
        // 第二步，过滤集合类中的元素。依据：不存在属性uidPk的元素会被过滤，或uidPk有值，或uidPk值大于0
        CollectionUtils.filter(c, new Predicate() {

            public boolean evaluate(Object o) {
                if (null == o || !(o instanceof PersistentDomainModel)) return false;

                Long uidPk = ((PersistentDomainModel) o).getUidPk();
                logger.debug("Current uid pk value in POJO object. [" + uidPk + "].");

                return null == uidPk || uidPk.intValue() < 1;
            }
        });

        // 第三步，如果集合类中没有任何符合第二步条件的元素，则终止后续操作。
        if (c.isEmpty()) {
            logger.info("No any elements are in Collection object.");

            return;
        }

        IsolatedWork isolatedWork = new IsolatedWork() {

            public synchronized Serializable doIsolatedWork(Connection conn) throws SQLException {
                // 第五步，循环集合类中元素，为每个元素添加自增长主键值
                for (Object e : c) {
                    String tableId = identifierNameResolver.populate(e);
                    logger.debug("Table id: [" + tableId + "]. Object type: ["
                            + getQualifiedName(e.getClass()) + "].");

                    IdentifierHolder holder = find(tableId);
                    if (holder == null) {
                        holder = new HiloIdentifierHolder(maxLoVal);
                        ((HiloIdentifierHolder) holder).setInsertSql(insertSql);
                        ((HiloIdentifierHolder) holder).setSelectSql(selectSql);
                        ((HiloIdentifierHolder) holder).setUpdateSql(updateSql);

                        boolean b = put(tableId, holder);
                        logger.debug("Add a new IdentifierHolder object, add result: [" + b + "].");
                    }

                    Serializable ser = holder.generate(tableId, conn);
                    logger.debug("Current id is: [" + ser + "].");

                    // 第六步，设置当前PO元素的主键值
                    new BeanWrapperImpl(e).setPropertyValue("uidPk", ser);
                }

                return null;
            }
        };

        // 第四步，打开数据库连接
        Connection conn = getConnection(dataSource);
        try {
            isolatedWork.doIsolatedWork(conn);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        } finally {
            // 第七步，释放当前数据库的连接
            releaseConnection(conn, dataSource);
            isolatedWork = null;
        }
    }

    private Collection convertToCollection(Object o) {
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

    public IdentifierHolderFactory getObject() throws Exception {
        return this;
    }

    public Class<?> getObjectType() {
        return HiloIdentifierHolderFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
