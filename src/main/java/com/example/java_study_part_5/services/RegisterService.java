package com.example.java_study_part_5.services;

import com.example.java_study_part_5.api.RegisterReq;
import com.example.java_study_part_5.api.RegisterResp;
import com.example.java_study_part_5.api.dto.RegisterRespDto;
import com.example.java_study_part_5.exceptions.AccountNotFoundException;
import com.example.java_study_part_5.mapper.RegisterMapper;
import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.Register;
import com.example.java_study_part_5.models.dicts.AccountPool;
import com.example.java_study_part_5.models.dicts.RegisterType;
import com.example.java_study_part_5.repository.AccountPoolRepository;
import com.example.java_study_part_5.repository.ProductRegisterRepository;
import com.example.java_study_part_5.repository.ProductRegisterTypeRepository;
import com.example.java_study_part_5.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegisterService {

    private final ProductRepository productRepository;
    private final ProductRegisterTypeRepository registerTypeRepository;

    private final ProductRegisterRepository registerRepository;

    private final AccountPoolRepository accountPoolRepository;
    protected List<RegisterType> registerTypeDict = new ArrayList<>();

    public RegisterService(ProductRepository productRepository, ProductRegisterTypeRepository registerTypeRepository, ProductRegisterRepository registerRepository, AccountPoolRepository accountPoolRepository) {
        this.productRepository = productRepository;
        this.registerTypeRepository = registerTypeRepository;
        this.registerRepository = registerRepository;
        this.accountPoolRepository = accountPoolRepository;
    }

    public Optional<String> checkRegisterType(String registerType) {
        Optional<List<RegisterType>> registerTypeDict = registerTypeRepository.findByValue(registerType);
        if (registerTypeDict.isPresent()) {
            this.registerTypeDict = registerTypeDict.get();
            return Optional.empty();
        } else {
            String errorMsg = "КодПродукта " + registerType + " не найдено в Каталоге продуктов";
            return errorMsg.describeConstable();
        }
    }

    public Optional<Product> findProduct(Long instanceId) {
        return productRepository.findById(instanceId);
    }

    public Optional<String> checkDuplicateRegister(Long instanceId, String typeCode) {
        Optional<Product> product = productRepository.findById(instanceId);

        return registerRepository.findByProductAndType(product.get(), typeCode).stream()
                .map(Register::getType)
                .findFirst();

    }

    public RegisterResp addRegister(RegisterReq addRegister) {
        Optional<Product> product = productRepository.findById(addRegister.getInstanceId());
        Register register = RegisterMapper.toEntity(addRegister);
        register.setProduct(product.get());
        Optional<Account> account = getAccount(addRegister.getBranchCode(), addRegister.getCurrencyCode(), addRegister.getMdmCode(), addRegister.getPriorityCode(), addRegister.getRegistryTypeCode());
        if (account.isPresent()) {
            register.setAccount(account.get());

        } else {
            throw new AccountNotFoundException("Для регистра счёт не найден");
        }
        Register savedRegister = registerRepository.save(register);
        Long savedAccountId = savedRegister.getAccount().getId();

        return getRegisterResp(savedAccountId);
    }

    public Optional<Account> getAccount(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
        Optional<AccountPool> accountPool = accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode(branchCode, mdmCode, registryTypeCode, currencyCode, priorityCode);
        return accountPool.flatMap(pool -> pool.getAccounts().stream().findFirst());
    }

    public RegisterResp getRegisterResp(Long accountId) {
        RegisterRespDto registerRespDTO = new RegisterRespDto();
        registerRespDTO.setAccountId(accountId);
        return new RegisterResp(registerRespDTO);
    }
}
