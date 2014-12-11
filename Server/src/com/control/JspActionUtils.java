
package com.control;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JspActionUtils {

    // 自定义 token
    private String TOKEN = "jhy";

    private static final Log logger = LogFactory.getLog(JspActionUtils.class);// LOG4J打印

    public static int getMagicNum(HttpServletRequest request) {
        return (int) (Math.random() * 100);
    }

}
