
package com.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 页内跳转
 * 
 * @author jhy
 */
@Controller
@Scope("session")
public class RedirectControl {

    // 自定义 token
    private String TOKEN = "jhy";

    private static final Log logger = LogFactory.getLog(RedirectControl.class);// LOG4J打印

    /**
     * @param req
     * @param reponse
     * @return
     */
    @RequestMapping("/redirect_test")
    public String redirect(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取redirect页面！");

        return "redirect:/wx1/redirect2";
        /**
         * http://localhost:8080/SpringServer/wx1/redirect_test
         */
    }

    @RequestMapping("/redirect2")
    @ResponseBody
    public String redirect2(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取redirect2页面！");

        return "haha";// 如果返回值不标注@ResponseBody，则跳转的是页面
    }

}
