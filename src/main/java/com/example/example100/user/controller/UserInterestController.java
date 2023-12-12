package com.example.example100.user.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.service.UserService;
import com.example.example100.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInterestController {

    private final UserService userService;

    /**
     * 78. 관심사용자에 등록하는 api를 작성하시오.
     */
    @PutMapping("/api/user/{id}/interest")
    public ResponseEntity<?> interestUser(@PathVariable Long id,
                                          @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(userService.addInterestUser(id, email));
    }

    /**
     * 79. 관심사용자를 삭제하는 api를 작성하시오.
     */
    @DeleteMapping("/api/interest/{id}")
    public ResponseEntity<?> removeInterestUser(@PathVariable Long id,
                                                @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(userService.removeInterestUser(id, email));
    }
}
