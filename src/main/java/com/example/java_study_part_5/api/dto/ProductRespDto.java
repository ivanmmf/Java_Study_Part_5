package com.example.java_study_part_5.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductRespDto {
    private Long instanceId;
    private Long[] registerId;

    private Long[] supplementaryAgreementId;
}
