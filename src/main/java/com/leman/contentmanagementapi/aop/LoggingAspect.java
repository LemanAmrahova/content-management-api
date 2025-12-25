package com.leman.contentmanagementapi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.leman.contentmanagementapi.controller..*(..))")
    public void controllerLayer() {
    }

    @Pointcut("execution(* com.leman.contentmanagementapi.service..*(..))")
    public void serviceLayer() {
    }

    @Around("controllerLayer()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.debug("[CONTROLLER] - {} with args: {}", methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.debug("[CONTROLLER] - {} returned: {} (in {}ms)", methodName, result, duration);

        return result;
    }

    @Around("serviceLayer()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.debug("[SERVICE] - {} with args: {}", methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.debug("[SERVICE] - {} returned: {} (in {}ms)", methodName, result, duration);

        return result;
    }

    @AfterThrowing(pointcut = "controllerLayer()", throwing = "error")
    public void logControllerError(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().toShortString();
        String errorDetail = error.getMessage() != null ? error.getMessage() : error.getClass().getSimpleName();

        log.error("[CONTROLLER] - {} failed: {}", methodName, errorDetail);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "error")
    public void logServiceError(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().toShortString();
        String errorDetail = error.getMessage() != null ? error.getMessage() : error.getClass().getSimpleName();

        log.error("[SERVICE] - {} failed: {}", methodName, errorDetail);
    }

}
