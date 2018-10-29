package com.ifree.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerResolver {

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<String> handleRequestNotFoundException() {
        return new ResponseEntity<>(
                "RequestWithoutDuration has not been handled or there were no such request registered",
                HttpStatus.NOT_FOUND);
    }
}
