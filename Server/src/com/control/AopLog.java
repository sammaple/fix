
package com.control;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopLog {

    private static final Log logger = LogFactory.getLog(JspControl.class);// LOG4J打印

    // aop WXControl。java中aop_control方法
    @Pointcut("execution (* com.control.*.aop_control(..))")
    // @Pointcut("execution (* org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.handle(..))")
    // @Around("execution(* org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.handle(..))")
    public void pointcut() {

        logger.debug("aop测试开始");
    }

    /*
     * @Before("pointcut()") public void before(){ System.out.println("11"); }
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        System.out.print(target.getClass().getName());
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        /*
         * for (int i = 0; i < args.length; i++) { if (i == 0) ((Users)
         * args[i]).setUpwd("f"); args[i] = 3; }
         */
        HttpServletRequest req = (HttpServletRequest) args[0];
        req.setAttribute("jhy", "aop insert");
        ;
        logger.debug("URL参数 in为:" + req.getParameter("in"));
        logger.debug("aop测试成功");
    }
}
