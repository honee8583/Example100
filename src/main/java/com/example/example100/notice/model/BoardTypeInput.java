package com.example.example100.notice.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeInput {
    @NotBlank(message = "게시판 제목은 필수 항목입니다.")
    private String name;
}
