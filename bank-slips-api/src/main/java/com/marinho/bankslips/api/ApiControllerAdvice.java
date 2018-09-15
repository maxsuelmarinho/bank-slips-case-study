package com.marinho.bankslips.api;

import com.marinho.bankslips.exception.BankSlipNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BankSlipNotFoundException.class)
    public ResponseEntity<Object> handleBankSlipNotFound(BankSlipNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
