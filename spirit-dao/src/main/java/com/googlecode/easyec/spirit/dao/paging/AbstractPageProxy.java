package com.googlecode.easyec.spirit.dao.paging;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 抽象的分页代理类。
 *
 * @author JunJie
 */
public abstract class AbstractPageProxy<T extends Page> implements PageProxy<T> {

    protected class PageInvocationHandler implements InvocationHandler {

        private final Logger logger = LoggerFactory.getLogger(PageInvocationHandler.class);
        private Page page;

        public PageInvocationHandler(Page page) {
            Assert.notNull(page, "Page implementation cannot be null.");
            this.page = page;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logger.isDebugEnabled()) {
                logger.debug("Proxy class: [" + proxy.getClass().getName() + "].");
                logger.debug("Method name: [" + method.getName() + "].");

                if (ArrayUtils.isNotEmpty(args)) {
                    for (int i = 0; i < args.length; i++) {
                        logger.debug("Parameter [" + i + "], [" + args[i] + "].");
                    }
                }
            }

            return method.invoke(page, args);
        }
    }
}
