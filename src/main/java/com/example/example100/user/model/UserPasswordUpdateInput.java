package com.example.example100.user.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateInput {
    @NotBlank(message = "기존 비밀번호는 필수 항목입니다.")
    private String password;

    @Size(min = 4, max = 20, message = "신규 비밀번호는 4자 이상 20자이하로 입력해주세요.")
    @NotBlank(message = "신규 비밀번호는 필수 항목입니다.")
    private String newPassword;
}
