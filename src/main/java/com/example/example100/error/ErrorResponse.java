package com.example.example100.error;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.ResponseErrorHandler;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String field;
    private String message;

    public static ErrorResponse of(FieldError e) {
        return ErrorResponse.builder()
                .field((e.getField()))
                .message(e.getDefaultMessage())
                .build();
    }

    public static List<ErrorResponse> of(List<ObjectError> errors) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        if (errors != null) {
            for (ObjectError error : errors) {
                errorResponses.add(ErrorResponse.of((FieldError) error));
            }
        }
        return errorResponses;
    }
}
