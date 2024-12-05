package com.erkan.interview_test_backend.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppError {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private AppError() {
        timestamp = LocalDateTime.now();
    }

    public AppError(HttpStatus status) {
        this();
        this.status = status;
    }

    public AppError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public AppError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Beklenmeyen bir hata oluştu";
        this.debugMessage = ex.getLocalizedMessage();
    }
}