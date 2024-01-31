package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.models.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement,Long> {
    Optional<Agreement> findByNumber(String number);
}
