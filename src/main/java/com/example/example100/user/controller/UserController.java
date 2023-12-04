package com.example.example100.user.controller;

import com.example.example100.error.ErrorResponse;
import com.example.example100.user.entity.User;
import com.example.example100.user.exception.UserAlreadyExistsException;
import com.example.example100.user.model.UserInput;
import com.example.example100.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    /**
     * 31. 사용자 등록시 입력값이 유효하지 않은 경우 예외를 발생시키는 기능을 작성하시오.
     * 입력값 : 이메일(ID), 이름, 비밀번호 연락처
     * 사용자 정의 에러 모델을 이용하여 에러를 리턴.
     */
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.getAllErrors().stream().forEach(e -> {
                errorResponses.add(ErrorResponse.of(((FieldError)e)));
            });
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 32. 사용자 정보를 입력받아서 저장하는 api를 작성하시오.
     * 입력값 : 이메일(중복확인), 이름, 비밀번호, 연락처, 가입일(현재일)
     */
    @PostMapping("/api/user2")
    public ResponseEntity<?> addUser2(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        Optional<User> savedUser = userRepository.findByEmail(userInput.getEmail());
        if (savedUser.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private List<ErrorResponse> checkErrors(Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.getAllErrors().stream().forEach(e -> {
                errorResponses.add(ErrorResponse.of(((FieldError)e)));
            });
            return errorResponses;
        }
        return null;
    }
}
