package com.example.example100.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLogCount {
    private Long id;
    private String email;
    private String userName;

    private Long noticeCount;
    private Long noticeLikedCount;
}
