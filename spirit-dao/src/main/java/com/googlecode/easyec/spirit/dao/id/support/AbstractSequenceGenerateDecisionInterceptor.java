package com.googlecode.easyec.spirit.dao.id.support;

import com.googlecode.easyec.spirit.dao.id.SequenceGenerator;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/**
 * 主键序列生成器的决定的抽象拦截类
 *
 * @author JunJie
 */
public abstract class AbstractSequenceGenerateDecisionInterceptor implements Ordered, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String before_expr
        = "execution(* com.*..*.dao.*Dao.create*(..)) && args(o)"
        + "||execution(* com.*..*.dao.*Dao.insert*(..)) and args(o)";

    protected SequenceGenerator sequenceGenerator;

    private int order;

    /**
     * 前置拦截主键序列生成的方法
     *
     * @param joinPoint 切入点对象
     * @param o         方法参数
     * @throws Exception
     */
    abstract public void before(JoinPoint joinPoint, final Object o) throws Exception;

    /**
     * 设置主键序列生成器对象实例
     *
     * @param sequenceGenerator <code>SequenceGenerator</code>对象实例
     */
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     * 设置AOP执行的顺序值。
     *
     * @param order 排序值。数值越大，优先权越低
     */
    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sequenceGenerator, "SequenceGenerator object is null.");
    }
}
