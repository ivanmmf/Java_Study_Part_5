package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.Agreement;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.dicts.AccountPool;
import com.example.java_study_part_5.repository.AccountPoolRepository;
import com.example.java_study_part_5.repository.AgreementRepository;
import com.example.java_study_part_5.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@DataJpaTest
@ActiveProfiles("test")
public class AgreementRepositoryTest {
    @Autowired
    AgreementRepository repository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void findByNumber(){
        Product product = Product.builder()
                .id(1L)
                .build();
        Agreement agreement = Agreement.builder()
                .id(1L)
                .product(product)
                .number("123")

                .build();
        Agreement agreement2 = Agreement.builder()
                .id(2L)
                .number("1234")
                .product(product)
                .build();
        product.setAgreements(List.of(agreement,agreement2));
        productRepository.save(product);
        repository.save(agreement);
        repository.save(agreement2);

        Optional<Agreement> agreement1 = repository.findByNumber("123");
        assertThat(agreement1.get().getNumber()).isEqualTo("123");
        Optional<Agreement> agreement3 = repository.findByNumber("5");
        assertThat(agreement3).isEqualTo(Optional.empty());
    }
}