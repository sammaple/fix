
package com.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")
public class JspControl {

    // 自定义 token
    private String TOKEN = "jhy";

    private static final Log logger = LogFactory.getLog(JspControl.class);// LOG4J打印

    /**
     * @param req
     * @param reponse
     * @return
     */
    @RequestMapping("/result")
    public String result(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取result页面！");

        req.getSession().setAttribute("success", true);
        req.getSession().setAttribute("msg", "哈哈");

        return "result";// 去掉了@ResponseBody，现在返回的result就是视图的模型
        /*
         * springmvc-servlet.xml
         */
        /*
         * <!-- 对模型视图名称的解析，即在模型视图名称添加前后缀 --> <bean
         * class="org.springframework.web.servlet.view.InternalResourceViewResolver"
         * p:prefix="/WEB-INF/jsp/" p:suffix=".jsp" />
         */
    }

    @RequestMapping("/model")
    public Object model(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取model页面！");

        req.getSession().setAttribute("success", true);
        req.getSession().setAttribute("msg", "哈哈");

        Map<String, String> value = new HashMap<String, String>();
        value.put("jhy", "fuck ufi");

        return new ModelAndView("model", value);

        /**
         * ModelAndView http://localhost:8080/SpringServer/wx1/model
         */

    }

    @RequestMapping("/model2")
    public Object model2(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取model2页面！");

        req.getSession().setAttribute("success", true);
        req.getSession().setAttribute("msg", "哈哈");

        Map<String, String> value = new HashMap<String, String>();
        value.put("jhy", "fuck ufi");

        return new ModelAndView("model", "mapname", value);// 返回mapname,在jsp中${mapname}直接使用

        /**
         * ModelAndView http://localhost:8080/SpringServer/wx1/model2
         */

    }
}
