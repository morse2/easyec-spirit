package com.googlecode.easyec.spirit.web.controller.sorts;

import java.io.Serializable;

/**
 * 排序的接口类。
 *
 * @author JunJie
 */
public interface Sort extends Serializable {

    public static enum SortTypes {

        /**
         * 排序为升序
         */
        ASC,
        /**
         * 排序为降序
         */
        DESC
    }

    /**
     * 返回排序方向
     *
     * @return ASC或DESC
     */
    SortTypes getType();

    /**
     * 返回被排序名字
     *
     * @return 排序字段
     */
    String getName();
}
