package com.example.java_study_part_5.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class RegisterNotFountException extends RuntimeException{

    private final String message;



    public RegisterNotFountException(String message) {
        this.message = message;
    }
}
