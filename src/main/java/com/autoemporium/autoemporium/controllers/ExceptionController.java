package com.autoemporium.autoemporium.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> exceptionHandler(MethodArgumentNotValidException e){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("asd", "qwe");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> exceptionHandler(HttpMessageNotReadableException e) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ggg", "fff");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getLocalizedMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }
}
