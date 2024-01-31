package com.example.java_study_part_5.controllers;

import com.example.java_study_part_5.api.RegisterReq;
import com.example.java_study_part_5.api.RegisterResp;
import com.example.java_study_part_5.api.dto.RegisterRespDto;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.services.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {
    @MockBean
    private RegisterService service;              // внедряем бин сервиса, который будем мокать
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void addRegisterFailValidation() throws Exception {
        var registerReq = RegisterReq.builder()
                        .branchCode("123")
                        .currencyCode("123")
                        .priorityCode("00")
                        .registryTypeCode("123")
                        .build();

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("Имена обязательных параметров: instanceId не заполнены"));

    }

    @Test
    void addRegisterProductNotFound() throws Exception {
        var registerReq = RegisterReq.builder()
                .instanceId(1L)
                .branchCode("123")
                .currencyCode("123")
                .priorityCode("00")
                .registryTypeCode("123")
                .build();
       given(service.findProduct(1L))
               .willReturn(Optional.empty());
        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorMsg").value("Для регистра не найден продукт с ID "+ registerReq.getInstanceId()));
}

    @Test
    void addRegisterRegisterDuplicate() throws Exception {
        var registerReq = RegisterReq.builder()
                .instanceId(1L)
                .branchCode("123")
                .currencyCode("123")
                .priorityCode("00")
                .registryTypeCode("123")
                .build();
        Product product = new Product();
        product.setId(1L);
        given(service.findProduct(1L))
                .willReturn(Optional.of(product));
        given(service.checkDuplicateRegister(1L,"123"))
                .willReturn(Optional.of("123"));

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("У продукта c ID " + registerReq.getInstanceId() + " уже есть регистр с типом " + registerReq.getRegistryTypeCode()));
    }

    @Test
    void addRegisterRegisterCheck() throws Exception {
        var registerReq = RegisterReq.builder()
                .instanceId(1L)
                .branchCode("123")
                .currencyCode("123")
                .priorityCode("00")
                .registryTypeCode("123")
                .build();
        var registerRespDto = RegisterRespDto.builder()
                .accountId(1L)
                .build();
        var registerResp = RegisterResp.builder()
                .data(registerRespDto)
                .build();
        Product product = new Product();
        product.setId(1L);
        given(service.findProduct(1L))
                .willReturn(Optional.of(product));
        given(service.checkDuplicateRegister(1L,"123"))
                .willReturn(Optional.empty());
        given(service.checkRegisterType(registerReq.getRegistryTypeCode()))
                .willReturn(Optional.empty());
        given(service.addRegister(ArgumentMatchers.any()))
                .willReturn(registerResp);

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(1L));
    }


}