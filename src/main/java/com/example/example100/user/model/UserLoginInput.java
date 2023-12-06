package com.example.example100.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginInput {
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @Size(min = 4, message = "비밀번호는 4자 이상 입력해야합니다.")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
