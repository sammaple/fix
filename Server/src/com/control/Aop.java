
package com.control;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aop {
    public Aop() {
        System.out.println("Aop");
    }

    // @Around("within(org.springframework.web.bind.annotation.support.HandlerMethodInvoker..*)")
    // 姜海洋，按照网上的介绍截取AnnotationMethodHandlerAdapter
    // AOP，不起作用，还是com.control起作用了，前提是将control的扫描放入
    // spring-servlet.xml由
    // dispatherservlet进行加载。不能由contextloaderlisten的applitocation.xml进行加载
    @Around("execution(* org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.handle(..))")
    public Object aa(ProceedingJoinPoint pjp) throws Throwable
    {
        System.out.println("aopaopaop");
        try {
            Object retVal = pjp.proceed();
            System.out.println(retVal);
            return retVal;
        } catch (Exception e) {
            System.out.println("异常");
            return null;
        }
    }
}
