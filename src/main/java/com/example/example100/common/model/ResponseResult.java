package com.example.example100.common.model;

import org.springframework.http.ResponseEntity;

public class ResponseResult {
    public static ResponseEntity<?> fail(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    public static ResponseEntity<?> success() {
        return ResponseEntity.ok().build();
    }
}
