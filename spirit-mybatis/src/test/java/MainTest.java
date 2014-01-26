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
        String sql = "select distinct a.field1, a.field2 from Table1 a join Table2 b on a.id = b.id";
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
