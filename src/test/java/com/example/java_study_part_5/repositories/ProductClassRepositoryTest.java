package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.dicts.ProductClass;
import com.example.java_study_part_5.repository.AgreementRepository;
import com.example.java_study_part_5.repository.ProductClassRepository;
import com.example.java_study_part_5.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProductClassRepositoryTest {
    @Autowired
    ProductClassRepository repository;
    @Autowired
    EntityManager entityManager;

    @Test
    void findByValue(){
        ProductClass productClass = ProductClass.builder()
                .id(1L)
                .value("123")
                .productRowName("12")
                .productRowCode("56")
                .gblName("45")
                .subclass_code("44")
                .subclass_name("45")
                .gblCode("32")
                .subclass_name("456")
                .build();
        ProductClass productClass2 = ProductClass.builder()
                .id(2L)
                .value("1234")
                .productRowName("1244")
                .productRowCode("564")
                .gblName("454")
                .subclass_code("444")
                .subclass_name("454")
                .gblCode("324")
                .subclass_name("45644")
                .build();
        repository.save(productClass);
        repository.save(productClass2);
        Optional<ProductClass> productClass1 = repository.findByValue("123");
        assertThat(productClass1.get().getId()).isEqualTo(1L);
        Optional<ProductClass> productClass3 = repository.findByValue("1235");
        assertThat(productClass3).isEmpty();
    }

}
