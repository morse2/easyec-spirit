package com.googlecode.easyec.spirit.mybatis.service.impl;

import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.Set;

import static org.springframework.aop.support.AopUtils.getTargetClass;
import static org.springframework.aop.support.AopUtils.isAopProxy;

/**
 * 业务委托类的代理类。
 * 该类负责为委托的业务类创建代理实现，
 * 用以实现代理接口的公共方法。
 *
 * @author JunJie
 */
final class DelegateServiceProxy {

    public static DelegateService createProxy(Object targetBean, DelegateDao delegateBean, BeanFactory beanFactory) {
        Class cls = targetBean.getClass();
        if (isAopProxy(targetBean)) {
            cls = getTargetClass(targetBean);
        }

        Set<Class<?>> interfaces = ClassUtils.getAllInterfacesForClassAsSet(cls);
        interfaces.add(DelegateService.class);

        return (DelegateService) Proxy.newProxyInstance(
            DelegateServiceProxy.class.getClassLoader(),
            interfaces.toArray(new Class[interfaces.size()]),
            new DelegateServiceInvocationHandler(targetBean, delegateBean, beanFactory)
        );
    }
}
