package com.ssafy.stella_trip.user.aspect;


import com.ssafy.stella_trip.user.annotation.PasswordEncoded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordEncodingAspect {

    private final PasswordEncoder passwordEncoder;

    @Around("execution(* com.ssafy.stella_trip.user.service.UserService.*(..))")
    public Object encodePasswordIfPresent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) continue;

            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof PasswordEncoded) {
                    if (args[i] instanceof String) {
                        String raw = (String) args[i];
                        String encoded = passwordEncoder.encode(raw);
                        log.info("[AOP] raw password: {}", raw);
                        log.info("[AOP] encoded password: {}", encoded);
                        log.info("[AOP] passwordEncoder class: {}", passwordEncoder.getClass().getName());
                        args[i] = encoded;
                    } else {
                        Field passwordField = args[i].getClass().getDeclaredField("password");
                        passwordField.setAccessible(true);
                        String raw = (String) passwordField.get(args[i]);
                        String encoded = passwordEncoder.encode(raw);
                        log.info("[AOP] DTO raw password: {}", raw);
                        log.info("[AOP] DTO encoded password: {}", encoded);
                        passwordField.set(args[i], encoded);
                    }
                }
            }
        }

        return joinPoint.proceed(args);
    }
}