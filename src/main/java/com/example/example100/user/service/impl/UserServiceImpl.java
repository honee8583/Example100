package com.example.example100.user.service.impl;

import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.entity.User;
import com.example.example100.user.entity.UserInterest;
import com.example.example100.user.model.UserLogCount;
import com.example.example100.user.model.UserNoticeCount;
import com.example.example100.user.model.UserStatus;
import com.example.example100.user.model.UserSummary;
import com.example.example100.user.repository.UserCustomRepository;
import com.example.example100.user.repository.UserInterestRepository;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserInterestRepository userInterestRepository;

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
}
