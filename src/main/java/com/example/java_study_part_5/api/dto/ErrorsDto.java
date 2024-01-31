package com.example.java_study_part_5.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ErrorsDto {
    private @Valid List<ErrorDto> errors;

    public ErrorsDto(String errorMsg) {
        this.errors = List.of(new ErrorDto(errorMsg));
    }

    public ErrorsDto(@NotNull Collection<String> errorsMsg) {
        this.errors = (List)errorsMsg.stream().map(ErrorDto::new).collect(Collectors.toList());
    }




    public List<ErrorDto> getErrors() {
        return this.errors;
    }

    public String toString() {
        return "ErrorsDTO(errors=" + this.getErrors() + ")";
    }

    public ErrorsDto(List<ErrorDto> errors) {
        this.errors = errors;
    }

}

