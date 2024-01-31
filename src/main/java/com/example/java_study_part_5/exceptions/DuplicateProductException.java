package com.example.java_study_part_5.exceptions;

import lombok.Getter;

@Getter
public class DuplicateProductException extends RuntimeException{

    private final String message;


    public DuplicateProductException(String message) {
        this.message = message;
    }
}
