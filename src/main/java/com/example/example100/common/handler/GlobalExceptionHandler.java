package com.example.example100.common.handler;

import com.example.example100.common.exception.AuthFailException;
import com.example.example100.common.model.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<?> AuthFailExceptionHandler(AuthFailException e) {
        return ResponseResult.fail("[인증실패] " + e.getMessage());
    }
}
