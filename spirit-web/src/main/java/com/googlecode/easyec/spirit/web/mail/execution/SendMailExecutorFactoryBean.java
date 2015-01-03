package com.googlecode.easyec.spirit.web.mail.execution;

import com.googlecode.easyec.spirit.web.mail.MailService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 发送邮件的执行器对象工厂类
 *
 * @author JunJie
 */
public class SendMailExecutorFactoryBean implements FactoryBean<SendMailExecutor>, InitializingBean {

    private SendMailExecutor executor;

    private MailService mailService;

    public SendMailExecutor getObject() throws Exception {
        return executor;
    }

    public Class<?> getObjectType() {
        return SendMailExecutor.class;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * 设置邮件服务对象
     *
     * @param mailService 邮件服务对象实例
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void afterPropertiesSet() throws Exception {
        this.executor = new SendMailExecutor(mailService);
    }
}
