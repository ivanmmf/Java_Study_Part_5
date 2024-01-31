package com.example.java_study_part_5.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
public class ErrorDto {

    @NotEmpty
    private String errorMsg;

    public ErrorDto(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
