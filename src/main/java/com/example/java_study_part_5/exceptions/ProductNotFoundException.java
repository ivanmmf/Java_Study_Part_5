package com.example.java_study_part_5.exceptions;


import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException{
    private final String errorMsg;

    public ProductNotFoundException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
