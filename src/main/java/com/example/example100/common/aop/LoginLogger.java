package com.example.example100.common.aop;

import com.example.example100.logs.service.LogsService;
import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserLoginInput;
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
public class LoginLogger {
    private final LogsService logsService;

    @Around("execution(* com.example.example100..*.*Service*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("#######################");
        log.info("서비스 호출 전");
        log.info("#######################");

        Object result = joinPoint.proceed();

        if (joinPoint.getSignature().getName().equals("login")) {
            StringBuilder sb = new StringBuilder();
            sb.append("함수명: " + joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName() + "\n");
            sb.append("파라미터: ");

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (Object o : args) {
                    if (o instanceof UserLoginInput) {
                        sb.append(((UserLoginInput) o).toString() + "\n");
                        sb.append(((User)result).toString());
                    }
                }
            }
            logsService.add(sb.toString());
            log.info(sb.toString());
        }

        log.info("#######################");
        log.info("서비스 호출 후");
        log.info("#######################");

        return result;
    }
}
