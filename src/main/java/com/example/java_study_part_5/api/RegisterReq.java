package com.example.java_study_part_5.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReq {
    @NotNull
    private Long instanceId;

    private String registryTypeCode;

    private String accountType;

    private String currencyCode;

    private String branchCode;

    private String mdmCode;

    private String priorityCode;

    private String clientCode;

    private String trainRegion;

    private String counter;

    private String salesCode;


}
