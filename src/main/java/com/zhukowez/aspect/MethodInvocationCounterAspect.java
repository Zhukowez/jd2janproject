package com.zhukowez.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Aspect
public class MethodInvocationCounterAspect {

    private final ConcurrentMap<String, AtomicInteger> methodInvocationCounts = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.zhukowez.repository.*.*.*(..))")
    public void methodInvocationPointcut() {
    }

    @After("methodInvocationPointcut()")
    public void countMethodInvocation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toString();
        methodInvocationCounts.computeIfAbsent(methodName, k -> new AtomicInteger()).incrementAndGet();
    }

    public void printMethodInvocationCounts() {
        methodInvocationCounts.forEach((methodName, count) ->
                System.out.println("Method '" + methodName + "' was called " + count.get() + " times"));
    }
}
