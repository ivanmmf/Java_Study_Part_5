package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.models.dicts.RegisterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRegisterTypeRepository extends JpaRepository<RegisterType,Long> {
    Optional<List<RegisterType>> findByValueAndAccountType(String value, String accountType);
    Optional<List<RegisterType>>findByValue(String value);

}
