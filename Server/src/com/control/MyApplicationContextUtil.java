
package com.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyApplicationContextUtil implements ApplicationContextAware {
    private static final Log logger = LogFactory.getLog(MyApplicationContextUtil.class);// LOG4J打印

    private static ApplicationContext context;// 声明一个静态变量保存

    @Override
    public void setApplicationContext(ApplicationContext contex) throws BeansException {
        logger.info("setApplicationContext");
        context = contex;
        logger.info("setApplicationContext:" + contex);

        // PushMessageSender.getInstance().getDaoFromApplicationContext();
    }

    public static ApplicationContext getContext() {
        return context;
    }

}
