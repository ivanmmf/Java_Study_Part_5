package com.example.java_study_part_5.mapper;

import com.example.java_study_part_5.api.RegisterReq;
import com.example.java_study_part_5.models.Register;

public class RegisterMapper {
    public static Register toEntity(RegisterReq req) {
        Register register = new Register();
        register.setType(req.getRegistryTypeCode());

        return register;
    }
}
