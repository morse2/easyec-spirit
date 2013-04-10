package com.googlecode.easyec.spirit.mybatis.executor.result;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * 数字类型结果处理器类。
 * 此类主要处理int类型的返回值。
 *
 * @author JunJie
 */
public class NumberResultHandler implements ResultHandler {

    private int totalRecordCount;

    public void handleResult(ResultContext context) {
        Object o = context.getResultObject();
        if (o instanceof Number) {
            totalRecordCount = ((Number) o).intValue();
        }
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }
}
