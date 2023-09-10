package com.pi.Centrale_Achat.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggingAspect {
    @After(" execution(* com.pi.Centrale_Achat.*.*.*(..)) ")

    public void logMethodExit(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName(); // Get the fully qualified class name
        String methodName = signature.getName();

        // Extract the package from the class name
        int lastDotIndex = className.lastIndexOf('.');
        String packageName = lastDotIndex >= 0 ? className.substring(0, lastDotIndex) : "";

        log.info("Out of method " + methodName + " in class " + className);
        log.info("Package: " + packageName);
    }

}
