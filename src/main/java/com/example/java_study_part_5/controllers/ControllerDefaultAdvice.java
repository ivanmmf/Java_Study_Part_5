package com.example.java_study_part_5.controllers;

import com.example.java_study_part_5.api.dto.ErrorDto;
import com.example.java_study_part_5.api.dto.ErrorsDto;
import com.example.java_study_part_5.exceptions.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerDefaultAdvice extends ResponseEntityExceptionHandler {
    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request) {

        List<String> errorFields = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorFields.add(error.getField());
        }
        String errorMsg = "Имена обязательных параметров: " + String.join(", ", errorFields) + " не заполнены";

        ErrorDto error = new ErrorDto(errorMsg);


        return handleExceptionInternal(ex, error, headers, status, request);

    }

    @ExceptionHandler({DuplicateProductException.class})
    public ResponseEntity<ErrorDto> handle(DuplicateProductException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({DuplicateAgreementException.class})
    public ResponseEntity<ErrorsDto> handle(DuplicateAgreementException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorsDto(ex.getErrors()));
    }

    @ExceptionHandler({RegisterNotFountException.class})
    public ResponseEntity<ErrorDto> handle(RegisterNotFountException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<ErrorDto> handle(AccountNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ex.getErrorMsg()));
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ErrorDto> handle(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ex.getErrorMsg()));
    }

    @ExceptionHandler({DuplicateRegisterException.class})
    public ResponseEntity<ErrorDto> handle(DuplicateRegisterException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getErrorMsg()));
    }

}