package com.example.example100.user.repository;

import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    int countByEmail(String email);

    Optional<User> findByIdAndPassword(Long id, String password);

    Optional<User> findByUserNameAndPhone(String userName, String phone);

    List<User> findByEmailContainsAndUserNameContainsAndPhoneContains(String email, String userName, String phone);

    int countByStatus(UserStatus userStatus);

    @Query("select u from User u where u.regDate between :startDate and :endDate")
    List<User> findUsersJoinedToday(LocalDateTime startDate, LocalDateTime endDate);
}
