package com.example.java_study_part_5.services;

import com.example.java_study_part_5.api.ProductReq;
import com.example.java_study_part_5.api.ProductResp;
import com.example.java_study_part_5.api.dto.InstanceArrangement;
import com.example.java_study_part_5.api.dto.ProductRespDto;
import com.example.java_study_part_5.exceptions.AccountNotFoundException;
import com.example.java_study_part_5.exceptions.ProductNotFoundException;
import com.example.java_study_part_5.mapper.AgreementMapper;
import com.example.java_study_part_5.mapper.ProductMapper;
import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.Agreement;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.Register;
import com.example.java_study_part_5.models.dicts.AccountPool;
import com.example.java_study_part_5.models.dicts.RegisterType;
import com.example.java_study_part_5.repository.AccountPoolRepository;
import com.example.java_study_part_5.repository.AgreementRepository;
import com.example.java_study_part_5.repository.ProductRegisterTypeRepository;
import com.example.java_study_part_5.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    List<RegisterType> registerTypeDict = new ArrayList<>();
    private final List<Register> registers = new ArrayList<>();
    private final ProductRepository productRepository;
    private final AgreementRepository agreementRepository;
    private final ProductRegisterTypeRepository registerTypeRepository;

    private final AccountPoolRepository accountPoolRepository;

    public ProductService(ProductRepository productRepository, AgreementRepository agreementRepository, ProductRegisterTypeRepository registerTypeRepository, AccountPoolRepository accountPoolRepository) {
        this.productRepository = productRepository;
        this.agreementRepository = agreementRepository;
        this.registerTypeRepository = registerTypeRepository;
        this.accountPoolRepository = accountPoolRepository;
    }

    public Optional<Product> findProduct(Long instanceId) {
        return productRepository.findById(instanceId);
    }


    public Optional<Product> productByNumber(String number) {
        return productRepository.findByNumber(number);
    }

    public Optional<List<String>> findDuplicateAgreements(List<String> agreementNumbers) {
        List<String> errorList = new ArrayList<>();

        List<Agreement> agreements = agreementRepository.findAll();
        List<String> numbers = agreements.stream().map(Agreement::getNumber).toList();

        Set<String> result = agreementNumbers.stream()
                .distinct()
                .filter(numbers::contains)
                .collect(Collectors.toSet());

        if (result.size() < 1) {
            return Optional.empty();
        } else {
            for (String number : result) {
                Optional<Product> product = productRepository.findById(agreementRepository.findByNumber(number).get().getProduct().getId());
                Long productId = product.get().getId();
                String errorMsg = "Параметр № Дополнительного соглашения (сделки) Number " + number + " уже существует для ЭП с ИД " + productId;
                errorList.add(errorMsg);
            }
            return Optional.of(errorList);
        }

    }

    public Optional<String> checkRegisterType(String registerType) {
        Optional<List<RegisterType>> registerTypeDict = registerTypeRepository.findByValueAndAccountType(registerType, "Клиентский");
        if (registerTypeDict.isPresent()) {
            this.registerTypeDict = registerTypeDict.get();
            return Optional.empty();
        } else {

            return ("КодПродукта " + registerType + " не найдено в Каталоге продуктов").describeConstable();
        }
    }


    public Optional<Account> getAccount(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
        Optional<AccountPool> accountPool = accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode(branchCode, mdmCode, registryTypeCode, currencyCode, priorityCode);
        return accountPool.flatMap(pool -> pool.getAccounts().stream().findFirst());
    }


    public ProductResp addProduct(ProductReq addProduct) {
        List<Agreement> agreements = AgreementMapper.toEntity(addProduct);
        Product product = ProductMapper.toEntity(addProduct);
        for (RegisterType registerType : registerTypeDict) {
            Register register = new Register();
            Optional<Account> account = getAccount(addProduct.getBranchCode(), addProduct.getIsoCurrencyCode(), addProduct.getMdmCode(), addProduct.getPriority(), registerType.getValue());
            register.setType(registerType.getValue());
            if (account.isPresent()) {
                register.setAccount(account.get());
                registers.add(register);
            } else {
                throw new AccountNotFoundException("Для типа регистра " + registerType.getValue() + " счёт не найден");
            }
        }
        product.setRegisters(registers);
        registers.forEach(register -> register.setProduct(product));
        product.setAgreements(agreements);
        agreements.forEach(agreement -> agreement.setProduct(product));
        Product savedProduct = productRepository.save(product);

        return getProductResp(savedProduct);
    }

    public ProductResp addAgreements(ProductReq addProduct) {
        Optional<Product> product = productRepository.findById(addProduct.getInstanceId());
        if (product.isPresent()) {
            List<Agreement> agreements = AgreementMapper.toEntity(addProduct);
            agreements.forEach(agreement -> agreement.setProduct(product.get()));
            agreementRepository.saveAll(agreements);
            List<Agreement> agreementTotal = product.get().getAgreements().stream().toList();
            product.get().setAgreements(agreementTotal);
            Long[] agreementIds = agreementTotal.stream().map(Agreement::getId).toList().toArray(Long[]::new);
            Long[] registerIds = product.get().getRegisters().stream().map(Register::getId).toList().toArray(Long[]::new);
            return getProductAgreementResp(agreementIds, registerIds, addProduct.getInstanceId());

        } else {
            throw new ProductNotFoundException("Не найден продукт с ID " + addProduct.getInstanceId());

        }
    }

    public ProductResp getProductResp(Product savedProduct) {
        ProductRespDto productRespDTO = new ProductRespDto();
        Long[] supplementaryAgreementId = savedProduct.getAgreements().stream()
                .map(Agreement::getId)
                .toArray(Long[]::new);
        productRespDTO.setSupplementaryAgreementId(supplementaryAgreementId);
        Long[] registerId = savedProduct.getRegisters().stream()
                .map(Register::getId)
                .toArray(Long[]::new);
        productRespDTO.setRegisterId(registerId);
        productRespDTO.setInstanceId(savedProduct.getId());
        return new ProductResp(productRespDTO);
    }

    public ProductResp getProductAgreementResp(Long[] agreementIds, Long[] registryIds, Long instanceId) {
        ProductRespDto productRespDTO = new ProductRespDto();
        productRespDTO.setSupplementaryAgreementId(agreementIds);
        productRespDTO.setRegisterId(registryIds);
        productRespDTO.setInstanceId(instanceId);
        return new ProductResp(productRespDTO);
    }

    public List<String> getNumbers(ProductReq req) {
        return req.getInstanceArrangement().stream()
                .map(InstanceArrangement::getNumber)
                .toList();
    }
}

