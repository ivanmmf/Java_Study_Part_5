package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.dicts.RegisterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByNumber(String number);
   // Optional<List<Product>> findByType(RegisterType type);
}
