package com.example.java_study_part_5.services;
import com.example.java_study_part_5.api.RegisterReq;
import com.example.java_study_part_5.api.RegisterResp;
import com.example.java_study_part_5.exceptions.AccountNotFoundException;
import com.example.java_study_part_5.mapper.RegisterMapper;
import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.models.Register;
import com.example.java_study_part_5.models.dicts.AccountPool;
import com.example.java_study_part_5.models.dicts.RegisterType;
import com.example.java_study_part_5.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    private ProductRepository productRepository;
    private AgreementRepository agreementRepository;
    private ProductRegisterTypeRepository registerTypeRepository;

    private AccountPoolRepository accountPoolRepository;
    private ProductRegisterRepository productRegisterRepository;

    private RegisterService registerService;
    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        agreementRepository = Mockito.mock(AgreementRepository.class);
        registerTypeRepository = Mockito.mock(ProductRegisterTypeRepository.class);
        productRegisterRepository = Mockito.mock(ProductRegisterRepository.class);
        accountPoolRepository = Mockito.mock(AccountPoolRepository.class);
        ProductClassRepository productClassRepository = Mockito.mock(ProductClassRepository.class);
        registerService = new RegisterService(productRepository, registerTypeRepository, productRegisterRepository, accountPoolRepository);
    }
    @Test
    void checkRegisterTypeSuccess() {
        RegisterType registerType = RegisterType.builder()
                .id(1L)
                .value("03.012.002_47533_ComSoLd")
                .build();
        given(registerTypeRepository.findByValue("03.012.002_47533_ComSoLd")).willReturn(Optional.of(List.of(registerType)));

        assertThat(registerService.checkRegisterType("03.012.002_47533_ComSoLd")).isEqualTo(Optional.empty());
    }

    @Test
    void checkRegisterTypeFail() {
        given(registerTypeRepository.findByValue("123")).willReturn(Optional.empty());
        assertThat(registerService.checkRegisterType("123").get()).isEqualTo("КодПродукта 123 не найдено в Каталоге продуктов");
    }

    @Test
    void findProductSuccess() {
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .startDateTime("123")
                .priority("00")
                .build();
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        assertThat(registerService.findProduct(1L).get().getNumber()).isEqualTo("123");
    }

    @Test
    void findProductFail() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThat(registerService.findProduct(1L)).isEqualTo(Optional.empty());
    }

    @Test
    void checkDuplicateRegisterFail() {
        Register register = Register.builder()
                .id(1L)
                .type("03.012.002_47533_ComSoLd")
                .build();
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .startDateTime("123")
                .priority("00")
                .registers(List.of(register))
                .build();

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRegisterRepository.findByProductAndType(product,"03.012.002_47533_ComSoLd")).willReturn(Optional.of(register));
        assertThat(registerService.checkDuplicateRegister(1L,"03.012.002_47533_ComSoLd").get()).isEqualTo("03.012.002_47533_ComSoLd");

    }

    @Test
    void checkDuplicateRegisterSuccess() {
        Register register = Register.builder()
                .id(1L)
                .type("03.012.002_47533_ComSoLd")
                .build();
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .startDateTime("123")
                .priority("00")
                .registers(List.of(register))
                .build();

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRegisterRepository.findByProductAndType(product,"03.012.002_47533_ComSoLd")).willReturn(Optional.empty());
        assertThat(registerService.checkDuplicateRegister(1L,"03.012.002_47533_ComSoLd")).isEqualTo(Optional.empty());

    }

    @Test
    void addRegisterSuccess() {
        RegisterReq registerReq = RegisterReq.builder()
                .instanceId(1L)
                .mdmCode("15")
                .branchCode("022")
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .priorityCode("00")
                .currencyCode("800")
                .build();
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .startDateTime("123")
                .priority("00")
                .build();
        Account account1 = Account.builder()
                .id(1L)
                .number("1234")
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("12345")
                .build();

        AccountPool accountPool = AccountPool.builder()
                .id(1L)
                .branchCode("022")
                .mdmCode("15")
                .priorityCode("00")
                .currencyCode("800")
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .accounts(List.of(account1,account2))
                .build();
        Register register = RegisterMapper.toEntity(registerReq);
        register.setAccount(account1);
        product.setRegisters(List.of(register));
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("022", "15", "03.012.002_47533_ComSoLd", "800", "00")).willReturn(Optional.of(accountPool));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        when(productRegisterRepository.save(any(Register.class))).thenReturn(register);
        RegisterResp registerResp = registerService.addRegister(registerReq);
        assertThat(registerResp.getData().getAccountId()).isEqualTo(1L);
    }

    @Test
    void addRegisterFail() {
        RegisterReq registerReq = RegisterReq.builder()
                .instanceId(1L)
                .mdmCode("15")
                .branchCode("022")
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .priorityCode("00")
                .currencyCode("800")
                .build();
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .startDateTime("123")
                .priority("00")
                .build();
        Register register = RegisterMapper.toEntity(registerReq);
        product.setRegisters(List.of(register));
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("022", "15", "03.012.002_47533_ComSoLd", "800", "00")).willReturn(Optional.empty());
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class,() -> registerService.addRegister(registerReq));
        assertEquals("Для регистра счёт не найден",thrown.getErrorMsg());
    }

    @Test
    void getAccountSuccess() {
        Account account1 = Account.builder()
                .id(1L)
                .number("1234")
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("12345")
                .build();

        AccountPool accountPool = AccountPool.builder()
                .id(1L)
                .branchCode("022")
                .mdmCode("15")
                .priorityCode("00")
                .currencyCode("800")
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .accounts(List.of(account1,account2))
                .build();
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("022", "15", "03.012.002_47533_ComSoLd", "800", "00")).willReturn(Optional.of(accountPool));
        assertThat(registerService.getAccount("022","800","15","00","03.012.002_47533_ComSoLd").get().getNumber()).isEqualTo("1234");
    }

    @Test
    void getAccountFail() {

        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("02245", "15", "03.012.002_47533_ComSoLd", "800", "00")).willReturn(Optional.empty());
        assertThat(registerService.getAccount("02245","800","15","00","03.012.002_47533_ComSoLd")).isEqualTo(Optional.empty());
    }

    @Test
     void getRegisterResp() {
        Long accountId = 1L;
        assertThat(registerService.getRegisterResp(accountId).getData().getAccountId()).isEqualTo(1L);
    }
}