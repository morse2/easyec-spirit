package com.googlecode.easyec.spirit.web.mail.execution;

import com.googlecode.easyec.spirit.web.mail.MailObject;
import com.googlecode.easyec.spirit.web.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 发送电子邮件的并发线程的执行类
 *
 * @author JunJie
 */
public class SendMailExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SendMailExecutor.class);

    private static final Object lock = new Object();

    private final BlockingQueue<MailObject> queue;
    private final ScheduledExecutorService  exec;

    private MailService mailService;

    protected SendMailExecutor(MailService mailService) {
        Assert.notNull(mailService, "MailService object is null.");
        this.mailService = mailService;

        queue = new LinkedBlockingDeque<MailObject>();
        exec = Executors.newScheduledThreadPool(10);

        // start monitor thread
        exec.scheduleAtFixedRate(new DefaultQueueConsumeTask(), 5, 10, SECONDS);
        exec.scheduleAtFixedRate(new DefaultQueueConsumeTask(), 10, 10, SECONDS);
    }

    public boolean isShutdown() {
        synchronized (lock) {
            return exec.isShutdown();
        }
    }

    public boolean prepare(MailObject bean) {
        synchronized (lock) {
            return null != bean && queue.add(bean);
        }
    }

    private MailObject takeQueue() {
        synchronized (lock) {
            if (!queue.isEmpty()) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            return null;
        }
    }


    @SuppressWarnings("unchecked")
    private class DefaultQueueConsumeTask implements Runnable {

        public void run() {
            MailObject bean = takeQueue();
            if (null == bean) {
                logger.trace("Queue is empty. So ignore logic else.");

                return;
            }

            try {
                // 打印邮件信息
                _printMailInfo(bean);
                // 执行发送邮件动作
                mailService.send(bean);
            } catch (MailException e) {
                logger.error(e.getMessage(), e);

                prepare(bean);
            }
        }

        /* 打印邮件信息 */
        private void _printMailInfo(MailObject mo) {
            if (logger.isInfoEnabled()) {
                logger.info("Printing mail message's information: [");
                logger.info("\tSubject: {" + mo.getSubject() + "}");
                logger.info("\tFrom: {" + mo.getFrom() + "}");
                logger.info("\tTo: {" + Arrays.toString(mo.getRecipients().toArray()) + "}");
                logger.info("\tCc: {" + Arrays.toString(mo.getCc().toArray()) + "}");
                logger.info("\tBcc: {" + Arrays.toString(mo.getBcc().toArray()) + "}");
                logger.info("\tText: {" + mo.getContent() + "}");
                logger.info("].\n");
            }
        }
    }
}
