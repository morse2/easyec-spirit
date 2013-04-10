package com.googlecode.easyec.spirit.dao.paging;

/**
 * 可执行计算的接口类。
 * <p>实现此类，表示可执行此类中的{@code compute}方法，
 * 以达到某中情况的固定结果。
 * </p>
 *
 * @author JunJie
 */
public interface PageComputable {

    /**
     * 执行计算以固化类的变量结果的方法。
     */
    void compute();
}
