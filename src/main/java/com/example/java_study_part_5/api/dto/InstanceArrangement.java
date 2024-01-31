package com.example.java_study_part_5.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InstanceArrangement {

    private String generalAgreementId;
    private String supplementaryAgreementId;
    private String arrangementType;
    private int schedulerJobId;
    @NotEmpty
    private String number;
    @NotNull
    private String openingDate;
    private Date closingdate;
    private Date cancelDate;

    private int validityDuration;
    private String cancellationReason;
    private String status;
    private Date interestCalculationDate;
    private  Float interestRate;
    private  Float coefficient;
    private  String coefficientAction;
    private  Float minimumInterestRate;
    private  String minimumInterestRateCoefficient;
    private  String minimumInterestRateCoefficientAction;
    private BigDecimal maximaInterestRate;
    private BigDecimal maximaInterestRateCoefficient;
    private String maximaInterestRateCoefficientAction;



}
