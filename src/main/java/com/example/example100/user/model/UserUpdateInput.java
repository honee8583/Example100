package com.example.example100.user.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInput {
    @Size(max = 20, message = "연락처는 최대 20자까지 입력해야합니다.")
    @NotBlank(message = "연락처는 필수 항목입니다.")
    private String phone;
}
