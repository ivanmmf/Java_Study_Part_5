package com.example.java_study_part_5.mapper;

import com.example.java_study_part_5.api.ProductReq;
import com.example.java_study_part_5.api.dto.InstanceArrangement;
import com.example.java_study_part_5.models.Agreement;

import java.util.ArrayList;
import java.util.List;

public class AgreementMapper {

    public static List<Agreement> toEntity (ProductReq req){
        List<Agreement> agreements = new ArrayList<>();
        for(InstanceArrangement agr:req.getInstanceArrangement()){
            Agreement agreement = new Agreement();
            agreement.setNumber(agr.getNumber());
            agreements.add(agreement);
        }

        return agreements;

    }
}
