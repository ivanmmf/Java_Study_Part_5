package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.JavaStudyPart5Application;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRegisterRepository extends JpaRepository<Register,Long> {

    Optional<Register> findByProductAndType(Product product,String typeCode);
}
