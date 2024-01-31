package com.example.java_study_part_5.controllers;

import com.example.java_study_part_5.api.ProductReq;
import com.example.java_study_part_5.api.ProductResp;
import com.example.java_study_part_5.api.dto.InstanceArrangement;
import com.example.java_study_part_5.api.dto.ProductRespDto;
import com.example.java_study_part_5.models.Agreement;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockBean
    private ProductService service;              // внедряем бин сервиса, который будем мокать
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addProductFailValidation() throws Exception {
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                //.contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .build();

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("Имена обязательных параметров: contractNumber не заполнены"));

    }

    @Test
    void addProductFailValidationAgreement() throws Exception {
        var agreement = InstanceArrangement.builder()
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("Имена обязательных параметров: instanceArrangement[0].number не заполнены"));

    }

    @Test
    void addProductDuplicateNumber() throws Exception {
        var agreement = InstanceArrangement.builder()
                .number("123")
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();
        Product product = new Product();
        given(service.productByNumber(productReq.getContractNumber()))
                .willReturn(Optional.of(product));
        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("Договор с номером " + productReq.getContractNumber() + " уже существует"));

    }

    @Test
    void addProductDuplicateAgreement() throws Exception {
        var agreement = InstanceArrangement.builder()
                .number("123")
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();
        given(service.getNumbers(ArgumentMatchers.any()))
                .willReturn(List.of("123"));
        given(service.productByNumber(productReq.getContractNumber()))
                .willReturn(Optional.empty());
        given(service.checkRegisterType(productReq.getRegisterType()))
                .willReturn(Optional.empty());
        given(service.findDuplicateAgreements((ArgumentMatchers.any())))
                .willReturn(Optional.of(List.of("Параметр № Дополнительного соглашения (сделки) Number 123  уже существует для ЭП с ИД 1")));
        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].errorMsg").value("Параметр № Дополнительного соглашения (сделки) Number 123  уже существует для ЭП с ИД 1"));
    }

    @Test
    void addProductControllerCheckRegister() throws Exception {
        var agreement = InstanceArrangement.builder()
                .number("123")
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();

        given(service.productByNumber(productReq.getContractNumber()))
                .willReturn(Optional.empty());
        given(service.checkRegisterType(productReq.getRegisterType()))
                .willReturn(("КодПродукта " + productReq.getRegisterType() + " не найдено в Каталоге продуктов").describeConstable());


        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("КодПродукта " + productReq.getRegisterType() + " не найдено в Каталоге продуктов"));
    }

    @Test
    void addProductControllerProductNotFound() throws Exception {
        var agreement = InstanceArrangement.builder()
                .number("123")
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .instanceId(1L)
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();

        given(service.findProduct(1L))
                .willReturn(Optional.empty());

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("Для регистра не найден продукт с ID "+ productReq.getInstanceId()));
    }
    @Test
    void addProductSuccess() throws Exception {
        var agreement = InstanceArrangement.builder()
                .number("123")
                .openingDate("sdf")
                .build();
        var productReq = ProductReq.builder()
                .priority("00")
                .productType("55")
                .productCode("456")
                .contractNumber("55")
                .registerType("dsd")
                .mdmCode("mdmCode")
                .contractDate("erer")
                .contractId(123)
                .branchCode("456")
                .isoCurrencyCode("123")
                .urgencyCode("123")
                .instanceArrangement(List.of(agreement))
                .build();
        var productRespDto = ProductRespDto.builder()
                .instanceId(1L)
                .registerId(new Long[]{1L})
                .supplementaryAgreementId(new Long[]{1L,2L})
                .build();

       var productResp = ProductResp.builder()
                       .data(productRespDto)
                       .build();
        given(service.getNumbers(ArgumentMatchers.any()))
                .willReturn(List.of("123"));
        given(service.productByNumber(productReq.getContractNumber()))
                .willReturn(Optional.empty());
        given(service.checkRegisterType(productReq.getRegisterType()))
                .willReturn(Optional.empty());
        given(service.findDuplicateAgreements((ArgumentMatchers.any())))
                .willReturn(Optional.empty());
        given(service.addProduct(ArgumentMatchers.any())).willReturn(productResp);

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.instanceId").value(1))
                .andExpect(jsonPath("$.data.registerId[0]").value(1))
                .andExpect(jsonPath("$.data.supplementaryAgreementId[0]").value(1))
                .andExpect(jsonPath("$.data.supplementaryAgreementId[1]").value(2));
    }



}




