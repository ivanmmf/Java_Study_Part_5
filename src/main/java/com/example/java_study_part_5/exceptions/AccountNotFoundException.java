package com.example.java_study_part_5.exceptions;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException{
    private final String errorMsg;

    public AccountNotFoundException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
