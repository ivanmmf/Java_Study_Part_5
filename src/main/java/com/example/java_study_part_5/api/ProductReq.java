package com.example.java_study_part_5.api;

import com.example.java_study_part_5.api.dto.AdditionalPropertiesVip;
import com.example.java_study_part_5.api.dto.InstanceArrangement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductReq {


    private Long instanceId;

    @NotEmpty
    private String productType;
    @NotEmpty
    private String productCode;
    @NotEmpty
    private String registerType;
    @NotNull
    private String mdmCode;
    @NotNull
    private String contractNumber;
    @NotNull
    private String contractDate;
    @NotNull
    private String priority;

    private float interestRatePenalty;

    private float minimalBalance;

    private float thresholdAmount;

    private String accountingDetailes;

    private String rateType;

    private float taxPercentageRate;

    private float technicalOverdraftLimitAmount;
    @NotNull
    private int contractId;
    @NotEmpty
    private String branchCode;
    @NotEmpty
    private String isoCurrencyCode;
    @NotEmpty
    private String urgencyCode = "00";

    private String referenceCode;

    private AdditionalPropertiesVip additionalPropertiesVip;
    @Getter
    @Setter
    @Valid
    private List<InstanceArrangement> instanceArrangement;

}
