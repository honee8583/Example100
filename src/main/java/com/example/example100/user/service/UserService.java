package com.example.example100.user.service;

import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserInput;
import com.example.example100.user.model.UserLogCount;
import com.example.example100.user.model.UserLoginInput;
import com.example.example100.user.model.UserNoticeCount;
import com.example.example100.user.model.UserPasswordResetInput;
import com.example.example100.user.model.UserSummary;
import java.util.List;

public interface UserService {
    UserSummary getUserStatusCount();

    List<User> getTodayJoinUsers();

    List<UserNoticeCount> getUserNoticeCount();

    List<UserLogCount> getUserLogCount();

    /**
     * 좋아요를 가장 만이 한 사용자 목록 리턴
     */
    List<UserLogCount> getUserLikeBest();

    ServiceResult addInterestUser(Long id, String email);

    ServiceResult removeInterestUser(Long id, String email);

    User login(UserLoginInput userLoginInput);

    ServiceResult addUser(UserInput userInput);

    ServiceResult resetPassword(UserPasswordResetInput userPasswordResetInput);
}
