package com.example.example100.common.interceptor;

import com.example.example100.common.exception.AuthFailException;
import com.example.example100.util.JWTUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class CommonInterceptor implements HandlerInterceptor {
    private final String AUTH_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("##########################");
        log.info(request.getMethod());
        log.info(request.getRequestURI());

        if (!JWTUtils.validJWT(request.getHeader(AUTH_HEADER))) {
            throw new AuthFailException("인증정보가 정확하지 않습니다.");
        }

        log.info("##########################");

        return true;
    }
}
