package com.example.java_study_part_5.exceptions;

import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DuplicateAgreementException extends RuntimeException{
    private final List<String> errors;

    public DuplicateAgreementException(List<String> errors) {
        this.errors = errors;
    }
}
