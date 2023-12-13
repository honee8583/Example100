package com.example.example100.user.service.impl;

import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.entity.User;
import com.example.example100.user.entity.UserPoint;
import com.example.example100.user.model.UserPointInput;
import com.example.example100.user.repository.UserPointRepository;
import com.example.example100.user.repository.UserRepository;
import com.example.example100.user.service.PointService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;

    @Override
    public ServiceResult addPoint(String email, UserPointInput userPointInput) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        userPointRepository.save(UserPoint.builder()
                .user(savedUser)
                .userPointType(userPointInput.getUserPointType())
                .point(userPointInput.getUserPointType().getValue())
                .build());

        return ServiceResult.success();
    }
}
