package com.filespace.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        log.error(e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<?> handleAccessDeniedException(Exception e) {
        log.error(e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.FORBIDDEN, e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({NoSuchElementException.class})
    protected ResponseEntity<?> handleNoSuchElementException(Exception e) {
        log.error(e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
