package com.googlecode.easyec.spirit.mybatis.lazyload;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.getBean;

/**
 * 支持Spring下上文配置的工厂类
 *
 * @author JunJie
 */
public class SpringConfigurationFactory {

    /**
     * 从Spring上下文中获取MyBatis的全局配置信息
     *
     * @return <code>Configuration</code>对象
     */
    public static Configuration getConfiguration() {
        return getBean(SqlSessionFactory.class).getConfiguration();
    }
}
