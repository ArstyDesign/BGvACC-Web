package com.bgvacc.web.aop;

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

  @Before("((execution(* com.bgvacc.web.controllers.*.*(..))"
          + " || execution(* com.bgvacc.web.controllers.*.*.*(..)))"
          + " && !execution(* com.bgvacc.web.controllers.vatsim.VatsimCoreController.getCallsignStatusCard*(..))"
          + " && !execution(* com.bgvacc.web.controllers.vatsim.VatsimMetarsController.getMetarAtIcao*(..))"
          + " && !execution(* com.bgvacc.web.controllers.UtilsController.getCurrentZuluTime*(..)))"
          + " || execution(* com.bgvacc.web.services.*.*(..))")
  public void methodBegin(JoinPoint joinPoint) {

    final String CLASS_NAME = joinPoint.getTarget().getClass().getSimpleName();
    final String METHOD_NAME = joinPoint.getSignature().getName();

    StringBuilder builder = new StringBuilder();
    builder.append(CLASS_NAME).append(" -> ").append(METHOD_NAME).append("() begin --");

    log.debug(builder.toString());
  }

  @Around("((execution(* com.bgvacc.web.controllers.*.*(..))"
          + " || execution(* com.bgvacc.web.controllers.*.*.*(..)))"
          + " && !execution(* com.bgvacc.web.controllers.vatsim.VatsimCoreController.getCallsignStatusCard*(..))"
          + " && !execution(* com.bgvacc.web.controllers.vatsim.VatsimMetarsController.getMetarAtIcao*(..))"
          + " && !execution(* com.bgvacc.web.controllers.UtilsController.getCurrentZuluTime*(..)))"
          + " || execution(* com.bgvacc.web.services.*.*(..))")
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
