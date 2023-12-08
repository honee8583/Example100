package com.example.example100.user.controller;

import com.example.example100.notice.repository.NoticeRepository;
import com.example.example100.user.entity.User;
import com.example.example100.user.entity.UserLoginHistory;
import com.example.example100.user.model.ResponseMessage;
import com.example.example100.user.model.UserListResponse;
import com.example.example100.user.model.UserLogCount;
import com.example.example100.user.model.UserNoticeCount;
import com.example.example100.user.model.UserSearchInput;
import com.example.example100.user.model.UserStatusInput;
import com.example.example100.user.model.UserSummary;
import com.example.example100.user.repository.UserLoginHistoryRepository;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;

    private final UserService userService;

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

    /**
     * 52. 사용자 정보를 삭제하는 api를 작성하시오.
     * 작성된 게시글이 있을 경우 예외처리.
     */
    @DeleteMapping("/api/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        User savedUser = user.get();

        if (noticeRepository.countByUser(savedUser) > 0) {
            return new ResponseEntity<>(ResponseMessage.fail("해당 회원이 작성한 게시글이 존재합니다."), HttpStatus.BAD_REQUEST);
        }

        userRepository.delete(savedUser);

        return ResponseEntity.ok().build();
    }

    /**
     * 53. 사용자가 로그인했을 때 접속이력이 저장된다고 했을 때, 접속이력을 조회하는 api를 작성하시오.
     * UserLoginHistory 엔티티를 통해서 구현
     */
    @GetMapping("/api/admin/user/login/history")
    public ResponseEntity<?> userLoginHistory() {
        List<UserLoginHistory> userLoginHistories = userLoginHistoryRepository.findAll();
        return new ResponseEntity<>(ResponseMessage.success(userLoginHistories), HttpStatus.OK);
    }

    /**
     * 54. 사용자의 접속을 제한하는 api를 작성하시오.
     */
    @PatchMapping("/api/admin/user/{id}/lock")
    public ResponseEntity<?> userLock(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User savedUser = user.get();
        if (savedUser.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속이 제한된 사용자입니다."), HttpStatus.BAD_REQUEST);
        }

        savedUser.setLockYn(true);
        userRepository.save(savedUser);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 55. 사용자의 접속제한을 해제하는 api를 작성하시오.
     */
    @PatchMapping("/api/admin/user/{id}/unlock")
    public ResponseEntity<?> userUnLock(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User savedUser = user.get();
        if (!savedUser.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속이 가능한 사용자입니다."), HttpStatus.BAD_REQUEST);
        }

        savedUser.setLockYn(false);
        userRepository.save(savedUser);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 56. 회원전체수와 상태별 회원수에 대한 정보를 리턴하는 api를 작성하시오.
     * 서비스 클래스를 이용해서 작성.
     */
    @GetMapping("/api/admin/user/status/count")
    public ResponseEntity<?> userStatusCount() {
        UserSummary userSummary = userService.getUserStatusCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userSummary));
    }

    /**
     * 57. 오늘 가입한 사용자 가입 목륵을 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/admin/user/today")
    public ResponseEntity<?> todayJoinUser() {
        List<User> users = userService.getTodayJoinUsers();

        return ResponseEntity.ok().body(ResponseMessage.success(users));
    }

    /**
     * 58. 사용자별 공지사항 게시글수를 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/admin/user/notice/count")
    public ResponseEntity<?> userNoticeCount() {
        List<UserNoticeCount> userNoticeCounts = userService.getUserNoticeCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCounts));
    }

    /**
     * 59. 사용자별 공지사항수와 좋아요수를 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/admin/user/notice/like/count")
    public ResponseEntity<?> userLogCount() {
        List<UserLogCount> userLogCounts = userService.getUserLogCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userLogCounts));
    }
}
