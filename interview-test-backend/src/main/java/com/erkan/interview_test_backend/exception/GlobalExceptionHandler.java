package com.erkan.interview_test_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.erkan.interview_test_backend.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex) {
        ErrorResponse error = ErrorResponse.builder().status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error("Dış API Hatası").message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error =
                ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("Sunucu Hatası").message("Beklenmeyen bir hata oluştu").build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
