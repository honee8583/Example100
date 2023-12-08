package com.example.example100.user.service;

import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserSummary;
import java.util.List;

public interface UserService {
    UserSummary getUserStatusCount();

    List<User> getTodayJoinUsers();
}
