package com.example.example100.user.service.impl;

import com.example.example100.mail.MailComponent;
import com.example.example100.common.exception.BizException;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.mail.entity.MailTemplate;
import com.example.example100.mail.model.MailInput;
import com.example.example100.mail.repository.MailTemplateRepository;
import com.example.example100.user.entity.User;
import com.example.example100.user.entity.UserInterest;
import com.example.example100.user.model.UserInput;
import com.example.example100.user.model.UserLogCount;
import com.example.example100.user.model.UserLoginInput;
import com.example.example100.user.model.UserNoticeCount;
import com.example.example100.user.model.UserPasswordResetInput;
import com.example.example100.user.model.UserStatus;
import com.example.example100.user.model.UserSummary;
import com.example.example100.user.repository.UserCustomRepository;
import com.example.example100.user.repository.UserInterestRepository;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.UserService;
import com.example.example100.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserInterestRepository userInterestRepository;
    private final MailTemplateRepository mailTemplateRepository;

    private final MailComponent mailComponent;

    private final String PASSWORD_RESET_TEMPLATE_ID = "USER_PASSWORD_RESET";
    private final String USER_JOIN_TEMPLATE_ID = "USER_JOIN";

    @Override
    public UserSummary getUserStatusCount() {
        long usingUserCount = userRepository.countByStatus(UserStatus.USING);
        long stopUserCount = userRepository.countByStatus(UserStatus.STOP);
        long totalCount = userRepository.count();

        return UserSummary.builder()
                .usingUserCount(usingUserCount)
                .stopUserCount(stopUserCount)
                .totalUserCount(totalCount)
                .build();
    }

    @Override
    public List<User> getTodayJoinUsers() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0);
        LocalDateTime endDate = startDate.plusDays(1);

        return userRepository.findUsersJoinedToday(startDate, endDate);
    }

    @Override
    public List<UserNoticeCount> getUserNoticeCount() {
        return userCustomRepository.findUserNoticeCount();
    }

    @Override
    public List<UserLogCount> getUserLogCount() {
        return userCustomRepository.findUserLogCount();
    }

    @Override
    public List<UserLogCount> getUserLikeBest() {
        return userCustomRepository.findUserLikeBest();
    }

    @Override
    public ServiceResult addInterestUser(Long id, String email) {
        Optional<User> interestUser = userRepository.findById(id);
        if (!interestUser.isPresent()) {
            return ServiceResult.fail("관심사용자가 존재하지 않습니다.");
        }
        User savedInterestUser = interestUser.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        if (savedUser.getId() == savedInterestUser.getId()) {
            return ServiceResult.fail("자기자신은 추가할 수 없습니다.");
        }

        if (userInterestRepository.countByUserAndInterestUser(savedUser, savedInterestUser) > 0) {
            return ServiceResult.fail("이미 관심사용자 목록에 추가되었습니다.");
        }

        UserInterest userInterest = UserInterest.builder()
                .interestUser(savedInterestUser)
                .user(savedUser)
                .regDate(LocalDateTime.now())
                .build();
        userInterestRepository.save(userInterest);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult removeInterestUser(Long id, String email) {
        Optional<UserInterest> userInterest = userInterestRepository.findById(id);
        if (!userInterest.isPresent()) {
            return ServiceResult.fail("관심사용자목록에 등록되어 있지 않습니다.");
        }
        UserInterest savedUserInterest = userInterest.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        if (savedUserInterest.getUser().getId() != savedUser.getId()) {
            return ServiceResult.fail("본인이 등록한 관심사용자가 아닙니다.");
        }

        userInterestRepository.delete(savedUserInterest);

        return ServiceResult.success();
    }

    @Override
    public User login(UserLoginInput userLoginInput) {
        Optional<User> user = userRepository.findByEmail(userLoginInput.getEmail());
        if (!user.isPresent()) {
            throw new BizException("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        if (!PasswordUtils.equalPassword(userLoginInput.getPassword(), savedUser.getPassword())) {
            throw new BizException("일치하는 정보가 없습니다.");
        }

        return savedUser;
    }

    @Override
    public ServiceResult addUser(UserInput userInput) {
        Optional<User> savedUser = userRepository.findByEmail(userInput.getEmail());
        if (savedUser.isPresent()) {
            throw new BizException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(PasswordUtils.encryptPassword(userInput.getPassword()))
                .phone(userInput.getPhone())
                .status(UserStatus.USING)
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Optional<MailTemplate> template = mailTemplateRepository.findByTemplateId(USER_JOIN_TEMPLATE_ID);
        template.ifPresent(e -> {
            MailInput mailInput = MailInput.builder()
                    .title(e.getTitle().replaceAll("\\{USER_NAME\\}", userInput.getUserName()))
                    .contents(e.getContents().replaceAll("\\{USER_NAME\\}", userInput.getUserName()))
                    .fromEmail(e.getSendEmail())
                    .fromName(e.getSendUserName())
                    .toEmail(userInput.getEmail())
                    .toName(userInput.getUserName())
                    .build();

            mailComponent.send(mailInput);
        });

        return ServiceResult.success();
    }

    @Override
    public ServiceResult resetPassword(UserPasswordResetInput userPasswordResetInput) {
        Optional<User> user = userRepository.findByEmailAndUserName(userPasswordResetInput.getEmail(),
                userPasswordResetInput.getUserName());
        if (!user.isPresent()) {
            throw new BizException("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        String passwordResetKey = UUID.randomUUID().toString();

        // 회원의 비밀번호 초기화 관련 값 세팅
        savedUser.setPasswordResetYn(true);
        savedUser.setPasswordResetKey(passwordResetKey);
        userRepository.save(savedUser);

        String serverUrl = "http://localhost:8080";

        Optional<MailTemplate> template = mailTemplateRepository.findByTemplateId(PASSWORD_RESET_TEMPLATE_ID);
        template.ifPresent(e -> {
            MailInput mailInput = MailInput.builder()
                    .title(e.getTitle().replaceAll("\\{USER_NAME\\}", savedUser.getUserName()))
                    .contents(e.getContents()
                            .replaceAll("\\{USER_NAME\\}", savedUser.getUserName())
                            .replaceAll("\\{SERVER_URL\\}", serverUrl)
                            .replaceAll("\\{RESET_PASSWORD_KEY\\}", passwordResetKey))
                    .fromEmail(e.getSendEmail())
                    .fromName(e.getSendUserName())
                    .toEmail(savedUser.getEmail())
                    .toName(savedUser.getUserName())
                    .build();

            log.info(mailInput.toString());

            mailComponent.send(mailInput);
        });

        return ServiceResult.success();
    }
}
