package com.log.puller.controller;

import com.log.puller.dto.FailureResponse;
import com.log.puller.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<FailureResponse> badRequestExceptionHandler(
            BadRequestException badRequestException) {
        log.error("Bad Request exception : ", badRequestException);
        FailureResponse failureResponse =
                FailureResponse.builder().message(badRequestException.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureResponse);
    }
}
