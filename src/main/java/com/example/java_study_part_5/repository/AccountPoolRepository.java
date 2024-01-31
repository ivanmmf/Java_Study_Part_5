package com.example.java_study_part_5.repository;

import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.dicts.AccountPool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountPoolRepository extends JpaRepository<AccountPool,Long> {
    Optional<AccountPool> findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode(String branchCode, String mdmCode, String registryTypeCode, String currencyCode, String priorityCode);
}
