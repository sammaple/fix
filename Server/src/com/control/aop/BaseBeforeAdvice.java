
package com.control.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * 前置通知。
 * 
 * @author yanbin
 */
public class BaseBeforeAdvice implements MethodBeforeAdvice {

    /**
     * method : 切入的方法 <br>
     * args ：切入方法的参数 <br>
     * target ：目标对象
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("===========进入beforeAdvice()============ \n");

        System.out.print("准备在" + target + "对象上用\n");
        System.out.print(method + "方法进行对 '\n");
        System.out.print(args[0] + "'进行删除！\n\n");

        System.out.println("要进入切入点方法了 \n");
    }

}
