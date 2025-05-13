package com.quickpick.ureca.v3.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleFileException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("티켓팅 실패");
    }
}
