package com.example.java_study_part_5.services;

import com.example.java_study_part_5.api.ProductReq;
import com.example.java_study_part_5.api.ProductResp;
import com.example.java_study_part_5.api.dto.InstanceArrangement;
import com.example.java_study_part_5.exceptions.AccountNotFoundException;
import com.example.java_study_part_5.mapper.ProductMapper;
import com.example.java_study_part_5.models.Account;
import com.example.java_study_part_5.models.Agreement;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductRepository productRepository;
    private AgreementRepository agreementRepository;
    private ProductRegisterTypeRepository registerTypeRepository;

    private AccountPoolRepository accountPoolRepository;

    private ProductService productService;



    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        agreementRepository = Mockito.mock(AgreementRepository.class);
        registerTypeRepository = Mockito.mock(ProductRegisterTypeRepository.class);
        accountPoolRepository = Mockito.mock(AccountPoolRepository.class);
        ProductClassRepository productClassRepository = Mockito.mock(ProductClassRepository.class);
        productService = new ProductService(productRepository, agreementRepository, registerTypeRepository, accountPoolRepository);
    }

    @Test
    void findProductSuccess() {
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .priority("00")
                .startDateTime("123")
                .build();
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(product));
        Optional<Product> product3 = productService.findProduct(1L);
        assertTrue(product3.isPresent());
        assertThat(product3.get().getNumber()).isEqualTo("123");
    }

    @Test
    void findProductFail() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        Optional<Product> product3 = productService.findProduct(1L);
        assertFalse(product3.isPresent());
    }

    @Test
    void productByNumberSuccess() {
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .priority("00")
                .startDateTime("123")
                .build();

        given(productRepository.findByNumber("123")).willReturn(Optional.ofNullable(product));
        Optional<Product> product3 = productService.productByNumber("123");
        assertTrue(product3.isPresent());
        assertThat(product3.get().getNumber()).isEqualTo("123");
    }

    @Test
    void productByNumberFail() {

        given(productRepository.findByNumber("123")).willReturn(Optional.empty());
        Optional<Product> product3 = productService.productByNumber("123");
        assertFalse(product3.isPresent());
    }

    @Test
    void findDuplicateAgreementsSuccess() {
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .priority("00")
                .startDateTime("123")
                .build();

        Agreement agreement1 = Agreement.builder()
                .id(1L)
                .number("123")
                .build();
        Agreement agreement2 = Agreement.builder()
                .id(2L)
                .number("1234")
                .build();

        agreement1.setProduct(product);
        agreement2.setProduct(product);
        given(agreementRepository.findAll()).willReturn(List.of(agreement1, agreement2));
        given(agreementRepository.findByNumber("123")).willReturn(Optional.of(agreement1));
        given(agreementRepository.findByNumber("1234")).willReturn(Optional.of(agreement1));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(product));
        Optional<List<String> >duplicateAgreement = productService.findDuplicateAgreements(List.of("123", "1234"));
        assertThat(duplicateAgreement.get().size()).isEqualTo(2);
        assertThat(duplicateAgreement.get().get(0)).isEqualTo("Параметр № Дополнительного соглашения (сделки) Number 123 уже существует для ЭП с ИД 1");

    }

    @Test
    void findDuplicateAgreementsFail() {
        Product product = Product.builder()
                .id(1L)
                .number("123")
                .priority("00")
                .startDateTime("123")
                .build();

        Agreement agreement1 = Agreement.builder()
                .id(1L)
                .number("123")
                .build();
        Agreement agreement2 = Agreement.builder()
                .id(2L)
                .number("1234")
                .build();
        agreement1.setProduct(product);
        agreement2.setProduct(product);
        given(agreementRepository.findAll()).willReturn(List.of(agreement1, agreement2));
        Optional<List<String>> duplicateAgreement = productService.findDuplicateAgreements(List.of("1238", "12348"));
        assertThat(duplicateAgreement).isEqualTo(Optional.empty());
    }


    @Test
    void checkRegisterTypeSuccess() {
        RegisterType registerType2 = RegisterType.builder()
                .id(2L)
                .registerTypeCode("1234")
                .registerTypeName("1234")
                .accountType("Клиентский")
                .value("qwertyasdf")
                .build();
        given(registerTypeRepository.findByValueAndAccountType("qwertyasdf", "Клиентский")).willReturn(Optional.of(List.of(registerType2)));

        Optional<String> checkRegister = productService.checkRegisterType("qwertyasdf");
        assertThat(checkRegister).isEqualTo(Optional.empty());
    }

    @Test
    void checkRegisterTypeFail() {
        given(registerTypeRepository.findByValueAndAccountType("123", "Клиентский")).willReturn(Optional.empty());

        Optional<String> checkRegister = productService.checkRegisterType("123");
        assertThat(checkRegister.get()).isEqualTo("КодПродукта 123 не найдено в Каталоге продуктов");
    }


    @Test
    void getAccountSuccess() {

        Account account1 = Account.builder()
                .id(1L)
                .number("1234")
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("1234")
                .build();

        AccountPool accountPool = AccountPool.builder()
                .id(1L)
                .branchCode("021")
                .mdmCode("13")
                .priorityCode("00")
                .currencyCode("500")
                .registryTypeCode("02.001.005_45343_CoDowFF")
                .build();
        accountPool.setAccounts(List.of(account1, account2));
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("021", "13", "02.001.005_45343_CoDowFF", "500", "00")).willReturn(Optional.of(accountPool));
        Optional<Account> account3 = productService.getAccount("021", "500", "13", "00", "02.001.005_45343_CoDowFF");
        assertThat(account3.get().getNumber()).isEqualTo("1234");
    }

    @Test
    void getAccountFail() {

        Account account1 = Account.builder()
                .id(1L)
                .number("1234")
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("1234")
                .build();

        AccountPool accountPool = AccountPool.builder()
                .id(1L)
                .branchCode("021")
                .mdmCode("13")
                .priorityCode("00")
                .currencyCode("500")
                .registryTypeCode("02.001.005_45343_CoDowFF")
                .build();
        accountPool.setAccounts(List.of(account1, account2));
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("02121", "13", "02.001.005_45343_CoDowFF", "500", "00")).willReturn(Optional.empty());
        Optional<Account> account3 = productService.getAccount("02121", "500", "13", "00", "02.001.005_45343_CoDowFF");
        assertTrue(account3.isEmpty());
    }
        @Test
        void addProductSuccess () {
            Agreement agreement1 = Agreement.builder()
                    .id(1L)
                    .number("123")
                    .build();
            Agreement agreement2 = Agreement.builder()
                    .id(2L)
                    .number("1234")
                    .build();
            Agreement agreement3 = Agreement.builder()
                    .id(3L)
                    .number("12345")
                    .build();
            InstanceArrangement instanceArrangement1 = InstanceArrangement.builder()
                    .number(agreement1.getNumber())
                    .openingDate("456")
                    .build();
            InstanceArrangement instanceArrangement2 = InstanceArrangement.builder()
                    .number(agreement2.getNumber())
                    .openingDate("456")
                    .build();

            ProductReq productReq = ProductReq.builder()
                    .isoCurrencyCode("800")
                    .mdmCode("15")
                    .branchCode("022")
                    .productType("Хранение ДМ.")
                    .productType("vc")
                    .contractNumber("ghjhjv")
                    .contractDate("dsds")
                    .registerType("03.012.002_47533_ComSoLd")
                    .priority("00")
                    .instanceArrangement(List.of(instanceArrangement1,instanceArrangement2))
                    .build();
            Product product = ProductMapper.toEntity(productReq);
            Register register = new Register();
            register.setId(1L);
            product.setRegisters(List.of(register));
            product.setId(1L);
            product.setAgreements(List.of(agreement1,agreement2,agreement3));
            when(productRepository.save(any(Product.class))).thenReturn(product);
            ProductResp productResp = productService.addProduct(productReq);
            assertThat(productResp.getData().getInstanceId()).isEqualTo(1);
            assertThat(productResp.getData().getSupplementaryAgreementId()[0]).isEqualTo(1);
            assertThat(productResp.getData().getSupplementaryAgreementId().length).isEqualTo(3);
            assertThat(productResp.getData().getRegisterId()[0]).isEqualTo(1);
        }

    @Test
    void addProductFail() {
        RegisterType registerType = RegisterType.builder()
                .id(1L)
                .value("03.012.002_47533_ComSoLd")
                .build();
        Agreement agreement1 = Agreement.builder()
                .id(1L)
                .number("123")
                .build();
        Agreement agreement2 = Agreement.builder()
                .id(2L)
                .number("1234")
                .build();
        Agreement agreement3 = Agreement.builder()
                .id(3L)
                .number("12345")
                .build();
        InstanceArrangement instanceArrangement1 = InstanceArrangement.builder()
                .number(agreement1.getNumber())
                .openingDate("456")
                .build();
        InstanceArrangement instanceArrangement2 = InstanceArrangement.builder()
                .number(agreement2.getNumber())
                .openingDate("456")
                .build();

        ProductReq productReq = ProductReq.builder()
                .isoCurrencyCode("800")
                .mdmCode("15")
                .branchCode("022")
                .productType("Хранение ДМ.")
                .productType("vc")
                .contractNumber("ghjhjv")
                .contractDate("dsds")
                .registerType("03.012.002_47533_ComSoLd")
                .priority("00")
                .instanceArrangement(List.of(instanceArrangement1,instanceArrangement2))
                .build();
        Product product = ProductMapper.toEntity(productReq);
        Register register = new Register();
        register.setId(1L);
        product.setRegisters(List.of(register));
        product.setId(1L);
        product.setAgreements(List.of(agreement1,agreement2,agreement3));
        productService.registerTypeDict = List.of(registerType);
        given(accountPoolRepository.findFirstByBranchCodeAndMdmCodeAndRegistryTypeCodeAndCurrencyCodeAndPriorityCode("022", "15", "03.012.002_47533_ComSoLd", "800", "00")).willReturn(Optional.empty());

        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class,() -> productService.addProduct(productReq));
        assertEquals("Для типа регистра " + registerType.getValue() + " счёт не найден",thrown.getErrorMsg());
    }
    @Test
    void getProductResp() {

        Agreement agreement1 = Agreement.builder()
                .id(1L)
                .number("123")
                .build();
        Agreement agreement2 = Agreement.builder()
                .id(2L)
                .number("1234")
                .build();
        Agreement agreement3 = Agreement.builder()
                .id(3L)
                .number("12345")
                .build();
        InstanceArrangement instanceArrangement1 = InstanceArrangement.builder()
                .number(agreement1.getNumber())
                .openingDate("456")
                .build();
        InstanceArrangement instanceArrangement2 = InstanceArrangement.builder()
                .number(agreement2.getNumber())
                .openingDate("456")
                .build();

        ProductReq productReq = ProductReq.builder()
                .isoCurrencyCode("800")
                .mdmCode("15")
                .branchCode("022")
                .productType("Хранение ДМ.")
                .productType("vc")
                .contractNumber("ghjhjv")
                .contractDate("dsds")
                .registerType("03.012.002_47533_ComSoLd")
                .priority("00")
                .instanceArrangement(List.of(instanceArrangement1,instanceArrangement2))
                .build();
        Product product = ProductMapper.toEntity(productReq);
        Register register = new Register();
        register.setId(1L);
        Register register1 = new Register();
        register1.setId(2L);
        product.setRegisters(List.of(register,register1));
        product.setId(1L);
        product.setAgreements(List.of(agreement1,agreement2,agreement3));
        assertThat(productService.getProductResp(product).getData().getInstanceId()).isEqualTo(1L);
        assertThat(productService.getProductResp(product).getData().getSupplementaryAgreementId().length).isEqualTo(3);
        assertThat(productService.getProductResp(product).getData().getRegisterId().length).isEqualTo(2);
        assertThat(productService.getProductResp(product).getData().getRegisterId()).contains(1L,2L);
        assertThat(productService.getProductResp(product).getData().getSupplementaryAgreementId()).contains(1L,2L,3L);
    }

    @Test
    void getProductAgreementResp() {
        List<Long> aggreementIds = List.of(1L,2L);
        List<Long> registerIds = List.of(1L,2L);
        Long instanceId = 1L;
        assertThat(productService.getProductAgreementResp(aggreementIds.toArray(Long[]::new),registerIds.toArray(Long[]::new),instanceId).getData().getInstanceId()).isEqualTo(1L);
        assertThat(productService.getProductAgreementResp(aggreementIds.toArray(Long[]::new),registerIds.toArray(Long[]::new),instanceId).getData().getSupplementaryAgreementId().length).isEqualTo(2);
        assertThat(productService.getProductAgreementResp(aggreementIds.toArray(Long[]::new),registerIds.toArray(Long[]::new),instanceId).getData().getRegisterId().length).isEqualTo(2);
        assertThat(productService.getProductAgreementResp(aggreementIds.toArray(Long[]::new),registerIds.toArray(Long[]::new),instanceId).getData().getRegisterId()).contains(1L,2L);
        assertThat(productService.getProductAgreementResp(aggreementIds.toArray(Long[]::new),registerIds.toArray(Long[]::new),instanceId).getData().getSupplementaryAgreementId()).contains(1L,2L);

    }

        @Test
        void addAgreements () {
            Agreement agreement1 = Agreement.builder()
                    .id(1L)
                    .number("123")
                    .build();
            Agreement agreement2 = Agreement.builder()
                    .id(2L)
                    .number("1234")
                    .build();
            Agreement agreement3 = Agreement.builder()
                    .id(3L)
                    .number("12345")
                    .build();
            InstanceArrangement instanceArrangement1 = InstanceArrangement.builder()
                    .number(agreement1.getNumber())
                    .openingDate("456")
                    .build();
            InstanceArrangement instanceArrangement2 = InstanceArrangement.builder()
                    .number(agreement2.getNumber())
                    .openingDate("456")
                    .build();

            ProductReq productReq = ProductReq.builder()
                    .instanceId(1L)
                    .isoCurrencyCode("800")
                    .mdmCode("15")
                    .branchCode("022")
                    .productType("Хранение ДМ.")
                    .productType("vc")
                    .contractNumber("ghjhjv")
                    .contractDate("dsds")
                    .registerType("03.012.002_47533_ComSoLd")
                    .priority("00")
                    .instanceArrangement(List.of(instanceArrangement1,instanceArrangement2))
                    .build();
            Product product = ProductMapper.toEntity(productReq);
            product.setId(1L);
            product.setAgreements(List.of(agreement1,agreement2,agreement3));
            Register register1 = new Register();
            register1.setId(1L);
            Register register2 = new Register();
            register2.setId(2L);
            product.setRegisters(List.of(register1,register2));
            given(productRepository.findById(1L)).willReturn(Optional.of(product));
            when(agreementRepository.saveAll(any())).thenReturn(List.of(agreement1,agreement2,agreement3));

            assertThat(productService.addAgreements(productReq).getData().getInstanceId()).isEqualTo(1L);

        }

        @Test
        void getNumbers () {
            Agreement agreement1 = Agreement.builder()
                    .id(1L)
                    .number("123")
                    .build();
            Agreement agreement2 = Agreement.builder()
                    .id(2L)
                    .number("1234")
                    .build();
            InstanceArrangement instanceArrangement1 = InstanceArrangement.builder()
                    .number(agreement1.getNumber())
                    .openingDate("456")
                    .build();
            InstanceArrangement instanceArrangement2 = InstanceArrangement.builder()
                    .number(agreement2.getNumber())
                    .openingDate("456")
                    .build();

            ProductReq productReq = ProductReq.builder()
                    .isoCurrencyCode("800")
                    .mdmCode("15")
                    .branchCode("022")
                    .productType("Хранение ДМ.")
                    .productType("vc")
                    .contractNumber("ghjhjv")
                    .contractDate("dsds")
                    .registerType("03.012.002_47533_ComSoLd")
                    .priority("00")
                    .instanceArrangement(List.of(instanceArrangement1,instanceArrangement2))
                    .build();
            assertThat(productService.getNumbers(productReq).size()).isEqualTo(2);
            assertThat(productService.getNumbers(productReq)).contains("123","1234");
        }
    }
