package com.example.example100.user.repository;

import com.example.example100.user.entity.User;
import com.example.example100.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    Long countByUserAndInterestUser(User user, User InterestUser);
}
