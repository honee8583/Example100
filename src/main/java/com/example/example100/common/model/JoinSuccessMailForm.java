package com.example.example100.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinSuccessMailForm {
    private final String fromName = "Admin";
    private String toEmail;
    private String toName;
    private final String title = "회원가입을 축하드립니다.";
    private final String contents = "회원가입을 축하드립니다.";
}
