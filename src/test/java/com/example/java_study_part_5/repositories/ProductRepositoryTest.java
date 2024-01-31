package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.repository.ProductRegisterTypeRepository;
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
public class ProductRepositoryTest {
    @Autowired
    ProductRepository repository;
    @Autowired
    EntityManager entityManager;
    @Test
    void findByNumber(){
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .number("1234")
                .build();
        repository.save(product);
        repository.save(product2);
        Optional<Product> product3 = repository.findByNumber("123");
        assertThat(product3.get().getNumber()).isEqualTo("123");
        Optional<Product> product4 = repository.findByNumber("1235");
        assertThat(product4).isEqualTo(Optional.empty());

    }
}
