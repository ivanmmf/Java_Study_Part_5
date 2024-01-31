package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.models.dicts.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductClassRepository extends JpaRepository<ProductClass,Long> {
    Optional<ProductClass>findByValue(String value);


}