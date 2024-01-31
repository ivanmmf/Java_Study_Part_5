package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.dicts.RegisterType;
import com.example.java_study_part_5.repository.ProductRegisterRepository;
import com.example.java_study_part_5.repository.ProductRegisterTypeRepository;
import com.example.java_study_part_5.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRegistryTypeRepositoryTest {
    @Autowired
    ProductRegisterTypeRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    void findByValueAndAccountType_Value() {
        RegisterType registerType = RegisterType.builder()
                .id(1L)
                .value("123")
                .registerTypeCode("123")
                .accountType("Клиентский")
                .build();
        RegisterType registerType2 = RegisterType.builder()
                .id(2L)
                .value("1234")
                .registerTypeCode("1234")
                .accountType("Клиентский")
                .build();
        repository.save(registerType);
        repository.save(registerType2);
        Optional<List<RegisterType>> registerType1 = repository.findByValueAndAccountType("123", "Клиентский");
        assertThat(registerType1.get().get(0).getValue()).isEqualTo("123");
        assertThat(registerType1.get().size()).isEqualTo(1);
        Optional<List<RegisterType>> registerType4 = repository.findByValue("1234");
        assertThat(registerType4.get().get(0).getValue()).isEqualTo("1234");
        assertThat(registerType4.get().size()).isEqualTo(1);
    }
}
