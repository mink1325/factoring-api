package com.mkcode.factoring.rest;

import com.mkcode.factoring.service.ContractNotFoundException;
import com.mkcode.factoring.service.ContractValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ContractValidationException.class, ContractNotFoundException.class})
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception exception) {
//        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(exception.getMessage());
//    }
}
