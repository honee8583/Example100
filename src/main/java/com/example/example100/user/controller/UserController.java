package com.example.example100.user.controller;

import com.example.example100.error.ErrorResponse;
import com.example.example100.notice.entity.Notice;
import com.example.example100.notice.model.NoticeResponse;
import com.example.example100.notice.repository.NoticeRepository;
import com.example.example100.user.entity.User;
import com.example.example100.user.exception.UserAlreadyExistsException;
import com.example.example100.user.exception.UserNotFoundException;
import com.example.example100.user.model.UserInput;
import com.example.example100.user.model.UserResponse;
import com.example.example100.user.model.UserUpdateInput;
import com.example.example100.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

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

    /**
     * 33. 사용자 정보를 수정하는 api를 작성하시오.
     * 사용자 정보가 없는 경우 UserNotFoundException 발생.
     * 연락처만 수정가능, 수정일은 현재 시간.
     */
    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateInput userUpdateInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        user.setPhone(userUpdateInput.getPhone());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 34. 사용자 정보를 조회의 기능을 수행하는 api를 작성하시오.
     * 비밀번호, 가입일, 회원정보 수정일은 리턴x
     */
    @GetMapping("/api/user/{id}")
    public ResponseEntity<?> findUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * 35. 본인이 작성한 공지사항 목록을 제공하는 api를 작성하시오.
     * 삭제일, 삭제자 아이디는 제공x.
     * 작성자정보를 모두 제공하지 않고, 아이디와 이름만 제공.
     */
    @GetMapping("/api/user/{id}/notice")
    public ResponseEntity<?> getUserNotice(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        Optional<List<Notice>> notices = noticeRepository.findByUser(user);
        List<NoticeResponse> noticeResponses = notices.get().stream().map(NoticeResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(noticeResponses);
    }
}
