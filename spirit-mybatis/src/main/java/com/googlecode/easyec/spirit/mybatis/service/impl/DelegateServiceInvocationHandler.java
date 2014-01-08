package com.googlecode.easyec.spirit.mybatis.service.impl;

import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection;

/**
 * 业务委托代理的方法回调处理类。
 * <p>
 * 此类负责拦截并处理接口
 * {@link DelegateService}中的方法，
 * 并且交由目标DelegateDao类来实现业务和数据层的调用。
 * </p>
 *
 * @author JunJie
 */
final class DelegateServiceInvocationHandler implements InvocationHandler, DelegateService {

    private static final Logger logger = LoggerFactory.getLogger(DelegateService.class);

    private Object      targetBean;
    private DelegateDao delegateDao;
    private BeanFactory beanFactory;

    public DelegateServiceInvocationHandler(Object targetBean, DelegateDao delegateDao, BeanFactory beanFactory) {
        this.targetBean = targetBean;
        this.delegateDao = delegateDao;
        this.beanFactory = beanFactory;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean b = method.getDeclaringClass().isAssignableFrom(DelegateService.class);
        logger.debug("The method of class is assignable from DelegateService? [{}].", b);

        return invokeJoinpointUsingReflection(b ? this : targetBean, method, args);
    }

    public void saveOrUpdate(GenericPersistentDomainModel model) throws DataPersistenceException {
        if (null == model.getUidPk()) create(model);
        else update(model);
    }

    @SuppressWarnings("unchecked")
    public void delete(GenericPersistentDomainModel model) throws DataPersistenceException {
        int i = delegateDao.deleteByPrimaryKey(model.getUidPk());
        logger.debug("Effect row of delete model. [{}].", i);
    }

    @SuppressWarnings("unchecked")
    public GenericPersistentDomainModel findByPK(Serializable uidPk) {
        return delegateDao.selectByPrimaryKey(uidPk);
    }

    public Page find(AbstractSearchFormBean formBean) {
        return delegateDao.find(beanFactory.getBean(PageDelegate.class).createPage(formBean));
    }

    public List search(AbstractSearchFormBean bean) {
        return delegateDao.find(bean.getSearchTerms());
    }

    public DelegateDao getDelegateDao() {
        return delegateDao;
    }

    @SuppressWarnings("unchecked")
    private void create(GenericPersistentDomainModel model) throws DataPersistenceException {
        try {
            int i = delegateDao.insert(model);
            logger.debug("Effect row of insert Model. [{}].", i);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void update(GenericPersistentDomainModel model) throws DataPersistenceException {
        try {
            int i = delegateDao.updateByPrimaryKey(model);
            logger.debug("Effect row of update Model. [{}].", i);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }
    }
}
