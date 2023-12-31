package com.example.example100.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.example100.board.entity.Board;
import com.example.example100.board.entity.BoardComment;
import com.example.example100.board.service.BoardService;
import com.example.example100.common.exception.BizException;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.error.ErrorResponse;
import com.example.example100.notice.entity.Notice;
import com.example.example100.notice.entity.NoticeLike;
import com.example.example100.notice.model.NoticeResponse;
import com.example.example100.notice.repository.NoticeLikeRepository;
import com.example.example100.notice.repository.NoticeRepository;
import com.example.example100.user.entity.User;
import com.example.example100.user.exception.PasswordNotMatchException;
import com.example.example100.user.exception.UserAlreadyExistsException;
import com.example.example100.user.exception.UserNotFoundException;
import com.example.example100.user.model.BoardCommentResponse;
import com.example.example100.user.model.UserFindInput;
import com.example.example100.user.model.UserInput;
import com.example.example100.user.model.UserLoginInput;
import com.example.example100.user.model.UserLoginToken;
import com.example.example100.user.model.UserPasswordResetInput;
import com.example.example100.user.model.UserPasswordUpdateInput;
import com.example.example100.user.model.UserPointInput;
import com.example.example100.user.model.UserResponse;
import com.example.example100.user.model.UserUpdateInput;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.PointService;
import com.example.example100.user.service.UserService;
import com.example.example100.util.JWTUtils;
import com.example.example100.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final String TOKEN_KEY = "Example100";

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;

    private final BoardService boardService;
    private final PointService pointService;
    private final UserService userService;

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

        User user = User.of(userInput);
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

    /**
     * 36. 사용자 등록시 이미 존재하는 이메일인경우 예외를 발생시키는 api를 작성하시오.
     * 동일한 이메일에 가입된 회원정보가 존재하는 경우 UserAlreadyExistsException 에외 발생.
     */
    @PostMapping("/api/user3")
    public ResponseEntity<?> addUser3(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        User user = User.of(userInput);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 37. 사용자의 비밀번호 수정 기능을 제공하는 api를 작성하시오.
     * 기존 비밀번호와 일치할경우 수정. 일치하지 않을 경우 PasswordNotMatchException 예외 발생.
     */
    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
                                            @RequestBody @Valid UserPasswordUpdateInput userPasswordUpdateInput,
                                            Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword(id, userPasswordUpdateInput.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));
        user.setPassword(userPasswordUpdateInput.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> passwordNotMatchExceptionHandler(PasswordNotMatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 38. 회원가입시 비밀번호를 암호화하여 저장하는 api를 작성하시오.
     */
    @PostMapping("/api/user4")
    public ResponseEntity<?> addUser4(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        userInput.setPassword(getEncryptedPassword(userInput.getPassword()));
        User user = User.of(userInput);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    private String getEncryptedPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * 39. 사용자 회원 탈퇴 기능에 대한 api를 작성하시오.
     * 회원정보가 존재하지 않은 경우 예외처리
     * 사용자가 등록한 공지사항이 있을 경우 회원삭제가 되지 않음.
     */
    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));
        try{
            userRepository.delete(user);
        } catch(DataIntegrityViolationException e) {
            return new ResponseEntity<>("제약조건에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("회원탈퇴중 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 40. 사용자 아이디(이메일)를 찾는 api를 작성하시오.
     * 이름과 전화번호에 해당하는 이메일을 찾는다.
     */
    @GetMapping("/api/user5")
    public ResponseEntity<?> findUser(@RequestBody UserFindInput userFindInput) {
        User user = userRepository.findByUserNameAndPhone(userFindInput.getUserName(), userFindInput.getPhone())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return ResponseEntity.ok().body(userResponse);
    }

    /**
     * 41. 사용자 비밀번호 초기화 요청(아이디 입력후 전화번호로 문자를 전송받음)의 기능을 수행하는 api를 작성하시오.
     * 아이디에 대한 정보 조회후 비밀번호를 초기화한 이후에 이를 문자전송.
     * 초기화 코드는 10자의 문자열로 지정.
     */
    @GetMapping("/api/user/{id}/password/reset")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        String resetPasswordCode = getResetPasswordCode();
        user.setPassword(getEncryptedPassword(resetPasswordCode));
        userRepository.save(user);

        String message = String.format("[%s]님의 임시 비밀번호가 [%s]로 초기화되었습니다.",
                user.getUserName(), resetPasswordCode);
        sendSMS(message);

        return ResponseEntity.ok().build();
    }

    private void sendSMS(String message) {
        log.info("[문자메시지전송]");
        log.info(message);
    }

    private String getResetPasswordCode() {
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 10);
    }

    /**
     * 42. 사용자가 좋아요한 공지사항 목록을 제공하는 api를 작성하시오.
     */
    @GetMapping("/api/user/{id}/notice/like")
    public List<NoticeLike> getLikedNoticeList(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        List<NoticeLike> noticeLikes = noticeLikeRepository.findByUser(user);

        log.info(noticeLikes.toString());

        return noticeLikes;
    }

    /**
     * 43. 사용자 이메일과 비밀번호를 통해서 가입된 회원정보와 일치하는지 확인(로그인)하는 api를 작성하시오.
     * 비밀번호가 일치하지 않은 경우 PasswordNotMatchException 발생.
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createJwtToken(@RequestBody @Valid UserLoginInput userLoginInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLoginInput.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        if (!PasswordUtils.equalPassword(userLoginInput.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 44. 사용자의 이메일과 비밀번호를 이용해서 JWT 토큰을 발행하는 api를 작성하시오.
     */
    @PostMapping("/api/user/login2")
    public ResponseEntity<?> createJwtToken2(@RequestBody @Valid UserLoginInput userLoginInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLoginInput.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        if (!PasswordUtils.equalPassword(userLoginInput.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        String token = JWT.create()
                .withExpiresAt(new Date())  // 기간설정필요
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("Example100".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }

    /**
     * 45. JWT 토큰 발행시 발행 유효기간을 1개월로 저장하는 api를 작성하시오.
     */
    @PostMapping("/api/user/login3")
    public ResponseEntity<?> createJwtToken3(@RequestBody @Valid UserLoginInput userLoginInput, Errors errors) {
        List<ErrorResponse> errorResponses = checkErrors(errors);
        if (errorResponses != null && errorResponses.size() > 0) {
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLoginInput.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        if (!PasswordUtils.equalPassword(userLoginInput.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        UserLoginToken token = JWTUtils.createToken(user);

        return ResponseEntity.ok().body(token);
    }

    /**
     * 46. JWT 토큰을 재발행하는 api를 작성하시오.
     * 이미 발행된 JWT토큰을 통해서 토큰을 재발행하는 로직 구현.
     * 정상적인 회원에대해서 재발행 진행.
     */
    @PatchMapping("/api/user/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = "";
        try{
            email = JWT.require(Algorithm.HMAC512(TOKEN_KEY))
                    .build()
                    .verify(token)
                    .getIssuer();
        } catch (JWTDecodeException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        String newToken = JWT.create()
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(1)))
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512(TOKEN_KEY.getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(newToken).build());
    }

    /**
     * 47. JWT 토큰에 대한 삭제를 요청하는 api를 작성하시오.
     */
    @DeleteMapping("/api/user/login")
    public ResponseEntity<?> removeToken(@RequestHeader("Authorization") String token) {
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        } catch (JWTDecodeException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        // 토큰삭제 관련 로직 추가 (쿠키, 세션 삭제 등등)

        return ResponseEntity.ok().build();
    }

    /**
     * 80. 내가 작성한 게시글 목록을 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/user/board/myboard")
    public ResponseEntity<?> getMyBoards(@RequestHeader("Authorization") String token) {
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        } catch (JWTDecodeException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        List<Board> myBoards = boardService.getMyBoardList(email);
        return ResponseResult.success(myBoards);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> bizExceptionHandler(BizException e) {
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 81. 내가 작성한 게시글의 코멘트 목록을 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/user/board/comment")
    public ResponseEntity<?> getMyComments(@RequestHeader("Authorization") String token) {
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        } catch (JWTDecodeException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        List<BoardCommentResponse> myBoardComments = boardService.getMyBoardCommentList(email);
        return ResponseResult.success(myBoardComments);
    }

    /**
     * 82. 사용자의 포인트 정보를 만들고 게시글을 작성할 경우 포인트를 누적하는 api를 작성하시오.
     */
    @PostMapping("/api/user/point")
    public ResponseEntity<?> userPoint(@RequestHeader("Authorization") String token,
                                       @RequestBody UserPointInput userPointInput) {
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        } catch (JWTDecodeException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseResult.result(pointService.addPoint(email, userPointInput));
    }

    /**
     * 95. 회원가입을 시도한 사용자에게 이메일을 전송하는 api를 작성하시오.
     */
    @PostMapping("/api/public/user")
    public ResponseEntity<?> addUserPublic(@RequestBody UserInput userInput) {
        ServiceResult result = userService.addUser(userInput);
        return ResponseResult.result(result);
    }

    /**
     * 96. 비밀번호 초기화시 사용자에게 인증코드를 전송하는 api를 작성하시오.
     */
    @PostMapping("/api/public/user/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserPasswordResetInput userPasswordResetInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = ErrorResponse.of(errors.getAllErrors());
            return ResponseResult.fail("입력값이 잘못되었습니다.", errorResponses);
        }

        ServiceResult result = null;
        try {
            result = userService.resetPassword(userPasswordResetInput);
            return ResponseResult.result(result);
        } catch (BizException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
}
