import com.googlecode.easyec.spirit.dao.dialect.impl.MySqlJdbcPageDialect;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-6
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = "classpath:spring/test/applicationContext-*.xml")
public class MainTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private PageDelegate pageDelegate;

    @Test
    public void testIt() {
        Page page = pageDelegate.createPage(1, pageDelegate.getPageSize());
        Assert.notNull(page);
    }

    @Test
    public void getCountSqlWithDistinct() {
//        String sql = "select distinct a.field1, a.field2 from Table1 a join Table2 b on a.id = b.id";
        String sql = "select distinct ca.UIDPK, ca.CASE_NO, ca.PARENT_ID, ca.ACCOUNT_NO, ca.CONSIGNMENT_NO, ca.CONTENT, ca.ESCALATION, ca.STATUS, ca.PRIORITY, ca.CONTACT_PERSON, ca.CONTACT_PHONE, ca.NOTIFY_ME, ca.FUNCTION_ID, ca.CATEGORY_ID, ca.DEPOT, REGION, ca.ASSIGNED_TERRITORY, ca.ASSIGNED_DEPOT, ca.ASSIGNED_REGION, ca.ASSIGNED_FUNCTION_ID, ca.ASSIGNED_WP_ID, ca.ASSIGN_TO, ca.DUE_TIME, ca.CLOSE_TIME, ca.CREATE_BY_UID, ca.RESPONSE_TIME, ca.CREATE_BY, ca.CREATE_TIME, ca.CUTOFF_TIME, ca.ASSIGN_TO_UID, ca.OVER_CLOSE , r.OVERDUED from \"CASE\" ca join ACTIVITY a on ca.uidpk = a.case_id join RESPONSE r on r.uidpk = a.uidpk WHERE ca.CREATE_BY_UID = ?";
        String countSql = new MySqlJdbcPageDialect().getCountSql(sql);
        System.out.println(countSql);
    }

    @Test
    public void getCountSqlWithUnion() {
        String sql = "select a.field1, a.field2 from Table1 a order by a.id desc union select b.field1, b.field2 from Table2 b";
        String countSql = new MySqlJdbcPageDialect().getCountSql(sql);
        System.out.println(countSql);
    }
}
