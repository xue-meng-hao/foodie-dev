package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * 切面表达式
     * execution 代表所要执行的表达式主体
     * 第一个 * 代表返回类型，*代表所有类型
     * 第二个 com.imooc.service 代表aop监控的类所在包
     * 第三个 .. 代表该包及该包类下所有方法
     * 第四个 * 代表子包的所有类名，*代表所有
     * 第五个 *(..) *代表类中的方法名,(..)方法中的参数,..代表方法中任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("==== 开始执行 {}.{}====", joinPoint.getClass(), joinPoint.getSignature());
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if (takeTime > 3000) {
            LOGGER.error("=====执行结束，耗时{}====", takeTime);
        } else if (takeTime > 1000) {
            LOGGER.warn("=====执行结束，耗时{}====", takeTime);
        } else {
            LOGGER.info("=====执行结束，耗时{}====", takeTime);
        }
        return result;
    }

}
