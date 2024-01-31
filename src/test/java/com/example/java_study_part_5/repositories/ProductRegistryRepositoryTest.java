package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.Agreement;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.Register;
import com.example.java_study_part_5.repository.ProductClassRepository;
import com.example.java_study_part_5.repository.ProductRegisterRepository;
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
public class ProductRegistryRepositoryTest {
    @Autowired
    ProductRegisterRepository repository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void findByProductAndType() {
        Product product = Product.builder()
                .id(1L)
                .build();
        Account account = Account.builder()
                .id(1L)
                .number("123")
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("1234")
                .build();

        Register register = Register.builder()
                .id(1L)
                .product(product)
                .type("Клиентский")
                .product(product)
                .account(account)
                .build();
        Register register2 = Register.builder()
                .id(2L)
                .product(product)
                .type("Клиентский2")
                .product(product)
                .account(account2)
                .build();
        productRepository.save(product);
        repository.save(register);
        repository.save(register2);
        Optional<Register> register1 = repository.findByProductAndType(product,"Клиентский");
        assertThat(register1.get().getType()).isEqualTo("Клиентский");
        Optional<Register> register3 = repository.findByProductAndType(product,"Клиентский3");
        assertThat(register3).isEqualTo(Optional.empty());

    }
}
