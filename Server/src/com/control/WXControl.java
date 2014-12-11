
package com.control;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.control.aop.BaseBusiness;
import com.control.aop.aspect.AspectBusiness;
import com.control.aop.aspectj.Business;
import com.entity.WXEntity;
import com.util.SHA1;
import com.util.Util;

@Controller
// @Scope("session")
public class WXControl {

    // 自定义 token
    private String TOKEN = "jhy";

    private static final Log logger = LogFactory.getLog(WXControl.class);// LOG4J打印

    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest req, HttpServletResponse reponse,
            WXEntity wx) throws UnsupportedEncodingException {

        logger.debug("微信开始验证！");

        if (Util.isStrEmpty(wx.getTimestamp()) || Util.isStrEmpty(wx.getNonce())
                || Util.isStrEmpty(wx.getEchostr()) || Util.isStrEmpty(wx.getSignature())) {

            logger.error("微信参数错误！");
            return "error";
        }

        String[] str = {
                TOKEN, wx.getTimestamp(), wx.getNonce()
        };

        String result = "";
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

        // 确认请求来至微信
        if (digest.equals(wx.getSignature())) {
            result = wx.getEchostr();
            logger.debug("验证成功！");
        } else {
            logger.error("验证失败！");
        }

        reponse.setContentType("text/html; charset=utf-8");
        reponse.setCharacterEncoding("utf-8");

        return new String(result.getBytes("utf-8"), "iso-8859-1");
    }

    @RequestMapping("/getjson")
    @ResponseBody
    public Object getjson(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取微信JSON数据!");

        WXEntity wx = new WXEntity();
        return wx;
        /*
         * http://localhost:8080/SpringServer/wx1/getjson 返回 json格式
         * {"signature":null,"timestamp":null,"nonce":null,"echostr":null}
         * 依赖于springmvc-servlet中mappingJacksonHttpMessageConverter
         */

    }

    @RequestMapping("/getjson_str")
    @ResponseBody
    public Object getjson_str(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取微信JSON数据str!");

        return "22";
        /*
         * http://localhost:8080/SpringServer/wx1/getjson_str 返回string格式 "22"
         */
    }

    @RequestMapping("/getjson_map")
    @ResponseBody
    public Object getjson_map(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取微信JSON数据map!");

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1", 2);
        map.put("2", null);// hashmap可以为空
        return map;
        /*
         * http://localhost:8080/SpringServer/wx1/getjson_map 返回json格式
         * {"2":null,"1":2}
         */
    }

    @RequestMapping("/getjson_list")
    @ResponseBody
    public Object getjson_list(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取微信JSON数据list!");

        List<String> list = new ArrayList<String>();
        list.add(null);
        list.add("hello");
        return list;
        /*
         * http://localhost:8080/SpringServer/wx1/getjson_list 返回json格式
         * [null,"hello"]
         */
    }

    /**
     * 如果修改为method = RequestMethod.POST则正常的GET获取失败
     * 
     * @list @map等不支持xml返回
     * @param req
     * @param reponse
     * @return
     */
    @RequestMapping(value = "/getjson_listc", method = RequestMethod.GET)
    @ResponseBody
    public Object getjson_listc(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug("获取微信JSON数据listc!");

        List<WXEntity> list = new ArrayList<WXEntity>();
        list.add(new WXEntity());
        WXEntity e = new WXEntity();
        e.setNonce("哈哈");
        list.add(e);
        return list;
        /*
         * http://localhost:8080/SpringServer/wx1/getjson_listc 返回json格式
         * [{"signature"
         * :null,"timestamp":null,"nonce":null,"echostr":null},{"signature"
         * :null,"timestamp":null,"nonce":"ad","echostr":null}]
         */
    }

    /**
     * 该函数可以支持xml 和 json的两种数据格式，记得在User 结构体上注解@ XmlRootElement
     * 同时springmvc-servlet中加入 mappingxmlHttpMessageConverter
     */
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public @ResponseBody
    User queryUser(@PathVariable("userid")
    long userID) {

        Calendar d = Calendar.getInstance();

        d.set(1987, 12, 9);

        User u = new User();

        u.setUserID(userID);

        u.setString("zhaoyang");

        u.setBirth(d.getTime());

        logger.debug("获取微信JSON数据listc!");
        return u;

        /*
         * C:\Documents and Settings\Administrator>curl -i -H
         * "Accept:application/json"
         * "http://localhost:8080/SpringServer/wx1/user/1"
         * {"userID":1,"string":"zhaoyang","birth":568691501343}
         */

        /*
         * C:\Documents and Settings\Administrator>curl -i -H
         * "Accept:application/xml"
         * "http://localhost:8080/SpringServer/wx1/user/1"
         */

        /*
         * <?xml version="1.0" encoding="UTF-8"
         * standalone="yes"?><user><birth>1988-01-09T0 9:51:29.656
         * +08:00</birth><string>zhaoyang</string><userID>1</userID></user>
         */
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleIOException(Exception ex, HttpServletRequest request) {
        logger.debug("异常处理!");
        return "error_request";// 自定义错误处理
        /**
         * http://localhost:8080/SpringServer/wx1/request_p?jhl=1
         */
    }

    @RequestMapping("/request_p")
    @ResponseBody
    public Object request_parameters(HttpServletRequest req, HttpServletResponse reponse,
            @RequestParam("jhy")
            int jhy) {

        logger.debug("参数强制需要!");

        int tt = jhy;
        return tt;
        /*
         * http://localhost:8080/SpringServer/wx1/request_p?jhy=1 返回json格式
         */
        /**
         * 09:47:12,000 DEBUG DispatcherServlet:933 - Last-Modified value for
         * [/SpringServer/wx1/request_p] is: -1 09:47:12,000 DEBUG
         * ExceptionHandlerExceptionResolver:132 - Resolving exception from
         * handler [public java.lang.Object
         * com.control.WXControl.request_parameters
         * (javax.servlet.http.HttpServletRequest
         * ,javax.servlet.http.HttpServletResponse,int)]:
         */
        /*
         * org.springframework.web.bind.MissingServletRequestParameterException:
         * Required int parameter 'jhy' is not present 09:47:12,000 DEBUG
         * ResponseStatusExceptionResolver:132 - Resolving exception from
         * handler [public java.lang.Object
         * com.control.WXControl.request_parameters
         * (javax.servlet.http.HttpServletRequest
         * ,javax.servlet.http.HttpServletResponse,int)]:
         * org.springframework.web.bind.MissingServletRequestParameterException:
         * Required int parameter 'jhy' is not present 09:47:12,015 DEBUG
         * DefaultHandlerExceptionResolver:132 - Resolving exception from
         * handler [public java.lang.Object
         * com.control.WXControl.request_parameters
         * (javax.servlet.http.HttpServletRequest
         * ,javax.servlet.http.HttpServletResponse,int)]:
         * org.springframework.web.bind.MissingServletRequestParameterException:
         * Required int parameter 'jhy' is not present
         */
    }

    @RequestMapping("/request_p2")
    @ResponseBody
    public Object request_p2(HttpServletRequest req, HttpServletResponse reponse,
            @RequestParam(value = "jhy", required = false)
            String jhy) {

        logger.debug("参数强制需要2!");
        if (jhy == null) {
            logger.debug("检查为空，重新赋值!");
            jhy = "222";
        }

        return jhy;
        /**
         * http://localhost:8080/SpringServer/wx1/request_p2?jhl=1
         */
    }

    /**
     * 【【【【重要:当@RequestParam的时候，不光是使用@ExceptionHandler来自定义处理返回错误界面或者字符串
     * 还可以使用AOP进行处理】】】】
     */

    // 使用到了cglib
    @RequestMapping("/aop_one")
    @ResponseBody
    public Object aop_one(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug(" aop_one!");

        ApplicationContext factory = null;
        ServletContext servletContext = null;
        servletContext = req.getSession().getServletContext();
        factory = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);

        BaseBusiness business = (BaseBusiness) factory.getBean("baseBusiness");
        business.delete("猫");
        return "aop_one";
        /**
         * http://localhost:8080/SpringServer/wx1/aop_one
         */
    }

    @RequestMapping("/aop_two")
    @ResponseBody
    public Object aop_schema_two(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug(" aop_two schema 配置方法!");

        ApplicationContext factory = null;
        ServletContext servletContext = null;
        servletContext = req.getSession().getServletContext();
        factory = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);

        AspectBusiness business = (AspectBusiness) factory.getBean("aspectBusiness");
        business.delete("狗");
        return "aop_two";
        /**
         * http://localhost:8080/SpringServer/wx1/aop_two
         */
    }

    @RequestMapping("/aop_three")
    @ResponseBody
    public Object aop_three(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug(" aop_three aspectj注释 配置方法!");

        ApplicationContext factory = null;
        ServletContext servletContext = null;
        servletContext = req.getSession().getServletContext();
        factory = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);

        Business business = (Business) factory.getBean("business");
        business.delete("鸭子");
        return "aop_three";
        /**
         * http://localhost:8080/SpringServer/wx1/aop_three
         */
    }

    @RequestMapping("/aop_control")
    @ResponseBody
    public Object aop_control(HttpServletRequest req, HttpServletResponse reponse) {

        logger.debug(" aop_control 在@control中使用aspectj!");

        ApplicationContext factory = null;
        ServletContext servletContext = null;
        servletContext = req.getSession().getServletContext();
        factory = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);

        return "aop_control--->" + req.getAttribute("jhy");// req.getAttribute("jhy")是从aop的函数中赋值获取的
        /**
         * http://localhost:8080/SpringServer/wx1/aop_control?in=footbal
         */
    }

}
