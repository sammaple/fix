
package com.control;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * @Description
 * @date 2012-3-30
 * @author jianghaiyang_Sw
 */
public class PushThreadStart implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(PushThreadStart.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // PushMessageSender.getInstance().stopthread();
        logger.info("push thread destroy");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // PushChangeNotifier.getInstance().setDaemon(true);
        // PushMessageSender.getInstance().setDaemon(true);
        // PushMonitor.getInstance().setDaemon(true);

        // PushMessageSender.getInstance().start();
        logger.info("push thread start");
    }

}
