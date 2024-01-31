package com.example.java_study_part_5.repositories;

import com.example.java_study_part_5.models.dicts.AccountPool;
import com.example.java_study_part_5.repository.AccountPoolRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
public class AccountPoolRepositoryTest {
    @Autowired
    AccountPoolRepository repository;
    @Autowired
    EntityManager entityManager;

    @Test
    void findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode(){
        AccountPool accountPool = AccountPool.builder()
                .id(1L)
                .priorityCode("00")
                .mdmCode("123")
                .registryTypeCode("123")
                .currencyCode("rub")
                .branchCode("022")
                .build();
        AccountPool accountPool2 = AccountPool.builder()
                .id(2L)
                .priorityCode("00")
                .mdmCode("1234")
                .registryTypeCode("1234")
                .currencyCode("rub")
                .branchCode("022")
                .build();
        repository.save(accountPool);
        repository.save(accountPool2);

        Optional<AccountPool> accountPool1 = repository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("022","123","123","rub","00");
        assertThat(accountPool1.get().getRegistryTypeCode()).isEqualTo("123");
        Optional<AccountPool> accountPool3 = repository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("0224","123","123","rub","00");
        assertThat(accountPool3).isEqualTo(Optional.empty());
    }
}
