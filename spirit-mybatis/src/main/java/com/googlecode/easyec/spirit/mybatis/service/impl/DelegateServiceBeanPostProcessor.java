package com.googlecode.easyec.spirit.mybatis.service.impl;

import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import com.googlecode.easyec.spirit.web.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * 委托类的业务对象Bean的后置处理类
 *
 * @author JunJie
 */
public class DelegateServiceBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DelegateServiceBeanPostProcessor.class);

    private ConfigurableListableBeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @SuppressWarnings("unchecked")
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DelegateService && !Proxy.isProxyClass(getTargetClass(bean))) {
            Class<DelegateDao> aClass = BeanUtils.findGenericType(bean, DelegateService.class, 0);
            Assert.notNull(aClass, "Generics type cannot be recognized, actual type is null.");

            DelegateDao ret = beanFactory.getBean(aClass);
            Assert.notNull(ret, "Cannot found bean in spring. Bean type is: ["
                + aClass.getName() + "].");

            DelegateService ds = DelegateServiceProxy.createProxy(bean, ret, beanFactory);
            logger.info("Create proxy service for class [{}].", aClass);
            return beanFactory.configureBean(ds, beanName);
        }

        return bean;
    }
}
