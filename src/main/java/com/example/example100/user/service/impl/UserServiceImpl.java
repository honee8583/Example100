package com.example.example100.user.service.impl;

import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserNoticeCount;
import com.example.example100.user.model.UserStatus;
import com.example.example100.user.model.UserSummary;
import com.example.example100.user.repository.UserCustomRepository;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;

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
}
