package com.example.example100.user.controller;

import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserListResponse;
import com.example.example100.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
