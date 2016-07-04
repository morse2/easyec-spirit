package com.googlecode.easyec.spirit.web.controller.formbean.terms.impl;

import com.googlecode.easyec.spirit.web.controller.formbean.annotations.SearchTermType;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsTransform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 模糊匹配条件的转换实现类。
 * <p>
 * 该类负责将字符串类型中包含有*符号的字符，
 * 转换成数据库对应的%字符，用以参加模糊匹配搜索。
 * </p>
 *
 * @author JunJie
 */
@SearchTermType({ String.class })
public class FuzzyMatchTermsTransform implements SearchTermsTransform {

    public Object transform(String id, Object val) {
        if (isNotBlank((String) val)) {
            return ((String) val).replaceAll("\\*", "%");
        }

        return val;
    }
}
