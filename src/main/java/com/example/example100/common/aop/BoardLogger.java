package com.example.example100.common.aop;

import com.example.example100.logs.service.LogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BoardLogger {
    private final LogsService logsService;

    @Around("execution(* com.example.example100..*.*Controller.detail(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("#######################");
        log.info("#######################");
        log.info("게시판 상세조회 호출 전");

        Object result = joinPoint.proceed();

        if (joinPoint.getSignature().getDeclaringTypeName().contains("BoardController")) {
            StringBuilder sb = new StringBuilder();

            sb.append("파라미터: ");
            Object[] args = joinPoint.getArgs();
            for (Object o : args) {
                sb.append(o.toString());
            }

            sb.append("\n");
            sb.append("결과: ");
            sb.append(result.toString());

            logsService.add(sb.toString());
            log.info(sb.toString());
        }

        log.info("#######################");
        log.info("#######################");
        log.info("게시판 상세조회 호출 후");

        return result;
    }
}
