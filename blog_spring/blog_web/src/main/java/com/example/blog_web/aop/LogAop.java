package com.example.blog_web.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * 日志切面类
 * @author palpitate
 * @date 2026/09/10
 */
@Slf4j
@Aspect
@Component
public class LogAop {



    @Around("execution(* com.example.blog_web.controller..*.*(..))")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("================================== 开始执行 ==================================");
        Long kstime = System.currentTimeMillis(); // 开始时间
        String className = joinPoint.getSignature().toString(); // 类名
        String methodName = joinPoint.getSignature().getName(); // 方法名

        Object result = joinPoint.proceed();

        String params = joinPoint.toShortString(); // 参数

        Long jstime = System.currentTimeMillis(); // 结束时间

        log.info("执行时间：{}ms", jstime - kstime);
        log.info("执行方法：{}.{}", className, methodName);
        log.info("方法参数：{}", params);
        log.info("方法返回值：{}", result);
        log.info("================================== 执行结束 ==================================");

        return result;
    }
}
