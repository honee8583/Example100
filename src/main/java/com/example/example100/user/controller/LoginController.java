package com.example.example100.user.controller;

import com.example.example100.common.exception.BizException;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.error.ErrorResponse;
import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserLoginInput;
import com.example.example100.user.model.UserLoginToken;
import com.example.example100.user.service.UserService;
import com.example.example100.util.JWTUtils;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /**
     * 83. 회원 로그인 히스토리 기능을 구현하는 api를 작성하시오.
     * 84. 로그인시 에러가 발생하는 경우 로그에 기록하는 기능 작성
     */
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginInput userLoginInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = ErrorResponse.of(errors.getAllErrors());
            return ResponseResult.fail("입력값이 정확하지 않습니다.", errorResponses);
        }

        UserLoginToken token = null;
        try{
            User user = userService.login(userLoginInput);
            token = JWTUtils.createToken(user);
        } catch (BizException e) {
            log.info("로그인 에러: " + e.getMessage());
            return ResponseResult.fail(e.getMessage());
        }

        if (token == null) {
            log.info("JWT 생성 에러");
            return ResponseResult.fail("JWT 생성에 실패하였습니다.");
        }

        return ResponseResult.success(token);
    }
}
