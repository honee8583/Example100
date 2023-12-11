package com.example.example100.common.model;

import com.example.example100.user.model.ResponseMessage;
import org.springframework.http.ResponseEntity;

public class ResponseResult {
    public static ResponseEntity<?> fail(String message) {
        return ResponseEntity.badRequest().body(ResponseMessage.fail(message));
    }

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok().body(ResponseMessage.success(data));
    }

    public static ResponseEntity<?> success() {
        return success(null);
    }

    public static ResponseEntity<?> result(ServiceResult result) {
        if (!result.isResult()) {
            return fail(result.getMessage());
        }
        return ResponseResult.success();
    }
}
