package com.marinho.bankslips.api;

import com.marinho.bankslips.exception.BankSlipNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BankSlipNotFoundException.class)
    protected ResponseEntity<Object> handleBankSlipNotFound(BankSlipNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus httpStatus,
                                                                  WebRequest request) {
        final ApiError apiError = new ApiError();
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(it -> apiError.addError(it.getField(), it.getDefaultMessage()));

        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
