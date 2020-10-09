package com.diy.sigmund.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 注解@Aspect标识该类为切面类
 *
 * @author ylm-sigmund
 * @since 2020/10/9 21:34
 */
@Component
@Aspect
public class PersonAspect {
//    /**
//     * 开会之前--找个位置坐下
//     */
//    @Before("execution(* com.diy.sigmund.aspect.ConferenceService.conference(..))")
//    public void takeSeats() {
//        System.out.println("找位置坐");
//    }
//
//    /**
//     * 开会之前--手机调成静音
//     */
//    @Before("execution(* com.diy.sigmund.aspect.ConferenceService.conference(..))")
//    public void silenceCellPhones() {
//        System.out.println("手机调成静音");
//    }
//
//    /**
//     * 开会之后--写会议总结报告
//     */
//    @After("execution(* com.diy.sigmund.aspect.ConferenceService.conference(..))")
//    public void summary() {
//        System.out.println("写会议总结报告");
//    }

    /**
     * =========================================================================
     * 从上面的执行代码可以看出切点execution表达式内容都是一样，
     * 我们可以通过@Pointcut进行优化。
     * =========================================================================
     */

//    /**
//     * 通过注解@Pointcut定义切点，conference()只是一个标识，无所谓是什么，
//     * 方法中内容本身也是空的，使用该切点的地方直接通过标识conference()引用切点表达式。
//     */
//    @Pointcut("execution(* com.diy.sigmund.aspect.ConferenceService.conference(..))")
//    public void conference() {}
//
//    /**
//     * 开会之前--找个位置坐下
//     */
//    @Before("conference()")
//    public void takeSeats() {
//        System.out.println("找位置坐");
//    }
//
//    /**
//     * 开会之前--手机调成静音
//     */
//    @Before("conference()")
//    public void silenceCellPhones() {
//        System.out.println("手机调成静音");
//    }
//
//    /**
//     * 开会之后--写会议总结报告
//     */
//    @After("conference()")
//    public void summary() {
//        System.out.println("写会议总结报告");
//    }

    /**
     * 通过注解@Pointcut定义切点，conference()只是一个标识，无所谓是什么，
     * 方法中内容本身也是空的，使用该切点的地方直接通过标识conference()引用切点表达式。
     */
    @Pointcut("execution(* com.diy.sigmund.aspect.ConferenceService.conference(..))")
    public void conference() {}

    @Around("conference()")
    public void testAround(ProceedingJoinPoint jp) {
        try {
            System.out.println("找位置坐");
            System.out.println("手机调成静音");
            jp.proceed();
            System.out.println("写会议总结报告");
        } catch (Throwable e) {
            System.out.println("开会不爽，打起来了");
        }
    }
}
