package com.asa.processminer.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@Slf4j
public class LoggingAspect {

    @Before("execution(public String get*())")
    public void loggingAdvice() {
        log.info("advice run.. getName method called");
    }
}
