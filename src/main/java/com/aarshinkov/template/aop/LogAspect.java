package com.aarshinkov.template.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Aspect
@Component
public class LogAspect {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Before("execution(* com.aarshinkov.template.controllers.*.*(..))"
          + " || execution(* com.aarshinkov.template.services.*.*(..))")
  public void methodBegin(JoinPoint joinPoint) {

    final String CLASS_NAME = joinPoint.getTarget().getClass().getSimpleName();
    final String METHOD_NAME = joinPoint.getSignature().getName();

    StringBuilder builder = new StringBuilder();
    builder.append(CLASS_NAME).append(" -> ").append(METHOD_NAME).append("() begin --");

    log.debug(builder.toString());
  }

  @Around("execution(* com.aarshinkov.template.controllers.*.*(..))"
          + " || execution(* com.aarshinkov.template.services.*.*(..))")
  public Object logExecTime(ProceedingJoinPoint pjp) throws Throwable {
    
    final String CLASS_NAME = pjp.getTarget().getClass().getSimpleName();
    final String METHOD_NAME = pjp.getSignature().getName();

    long startTime = System.nanoTime();
    Object output = pjp.proceed();
    BigDecimal elapsedTimeMillis = new BigDecimal(System.nanoTime() - startTime).divide(new BigDecimal(1000000));

    StringBuilder builder = new StringBuilder();

    builder.append(CLASS_NAME).append(" -> ").append(METHOD_NAME).append("() ended in ");
//    result += className + " -> " + methodName + "() ended in ";

    if (elapsedTimeMillis.doubleValue() < 1000) {
      builder.append(elapsedTimeMillis).append(" millis.");
    } else {
      builder.append(elapsedTimeMillis.divide(new BigDecimal(1000))).append(" seconds.");
    }

    builder.append(" --");
    log.debug(builder.toString());

    return output;
  }
}
