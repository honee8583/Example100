package com.example.example100.user.entity;

import com.example.example100.user.model.UserInput;
import com.example.example100.user.model.UserStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "USER_ENTITY")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String userName;
    private String password;
    private String phone;
    private UserStatus status;
    private boolean lockYn;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private boolean passwordResetYn;
    private String passwordResetKey;

    public static User of(UserInput userInput) {
        return User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();
    }
}
