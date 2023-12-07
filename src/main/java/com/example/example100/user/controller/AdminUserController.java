package com.example.example100.user.controller;

import com.example.example100.user.entity.User;
import com.example.example100.user.exception.UserNotFoundException;
import com.example.example100.user.model.ResponseMessage;
import com.example.example100.user.model.UserListResponse;
import com.example.example100.user.model.UserSearchInput;
import com.example.example100.user.model.UserStatusInput;
import com.example.example100.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserRepository userRepository;

    /**
     * 48. 사용자 목록과 사용자 수를 함께 내리는 api를 작성하시오.
     * {"totalCount": 3, "data": ...} 의 형식으로 리턴.
     */
    @GetMapping("/api/admin/user")
    public ResponseEntity<?> userList() {
        List<User> users = userRepository.findAll();
        long totalUserCount = userRepository.count();

        UserListResponse userListResponse = UserListResponse.builder()
                .totalCount(totalUserCount)
                .data(users)
                .build();

        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }

    /**
     * 49. 사용자의 상세 정보를 조회하는 api를 조건에 맞게 구현하시오.
     * {"header": {result: true|false, resultCode:String, message: error message or alert message, status: http result code}, "body": data}
     */
    @GetMapping("/api/admin/user/{id}")
    public ResponseEntity<?> userDetail(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(ResponseMessage.success(user));
    }

    /**
     * 50. 사용자 목록 조회에 대한 검색을 리턴하는 api를 작성하시오.
     * 이메일, 이름, 전화번호에 대한 결과를 리턴.
     */
    @GetMapping("/api/admin/user/search")
    public ResponseEntity<?> findUser(@RequestBody UserSearchInput userSearchInput) {
        List<User> users = userRepository.findByEmailContainsAndUserNameContainsAndPhoneContains(
                userSearchInput.getEmail(),
                userSearchInput.getUserName(),
                userSearchInput.getPhone()
        );

        return ResponseEntity.ok().body(ResponseMessage.success(users));
    }

    /**
     * 51. 사용자의 상태를 변경하는 api를 작성하시오.
     * 정상 : Using, 정지 : Stop
     */
    @PatchMapping("/api/admin/user/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody UserStatusInput userStatusInput) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        User savedUser = user.get();
        savedUser.setStatus(userStatusInput.getStatus());
        userRepository.save(savedUser);

        return ResponseEntity.ok().build();
    }
}
