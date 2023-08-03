package com.autoemporium.autoemporium.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> exceptionHandler(MethodArgumentNotValidException e){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Error-Message", "qwe");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> exceptionHandler(HttpMessageNotReadableException e) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Error-Message", "fff");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getLocalizedMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> exceptionHandler(IllegalArgumentException e) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Error-Message", "Invalid advertisement id");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getLocalizedMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(NoSuchElementException e) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Error-Message", "No such object exists");
        ResponseEntity<String> response = new ResponseEntity<>(Objects.requireNonNull(e.getLocalizedMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        return response;
    }

}


