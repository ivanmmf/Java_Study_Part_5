package com.example.java_study_part_5.api;

import com.example.java_study_part_5.api.dto.ProductRespDto;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResp {
private ProductRespDto data;
}
