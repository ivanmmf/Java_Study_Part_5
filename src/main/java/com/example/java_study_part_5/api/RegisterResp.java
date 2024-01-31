package com.example.java_study_part_5.api;

import com.example.java_study_part_5.api.dto.RegisterRespDto;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResp {
    private RegisterRespDto data;
}
