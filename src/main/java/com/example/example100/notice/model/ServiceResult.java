package com.example.example100.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResult {
    private boolean result;
    private String message;

    public static ServiceResult fail(String s) {
        return ServiceResult.builder()
                .result(false)
                .message(s)
                .build();
    }

    public static ServiceResult success() {
        return ServiceResult.builder()
                .result(true)
                .build();
    }
}
