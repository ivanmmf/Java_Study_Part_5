package com.example.java_study_part_5.controllers;

import com.example.java_study_part_5.api.RegisterReq;
import com.example.java_study_part_5.api.RegisterResp;
import com.example.java_study_part_5.exceptions.DuplicateRegisterException;
import com.example.java_study_part_5.exceptions.ProductNotFoundException;
import com.example.java_study_part_5.exceptions.RegisterNotFountException;
import com.example.java_study_part_5.models.Product;
import com.example.java_study_part_5.services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Validated
@RestController
@RequestMapping(value = "corporate-settlement-account/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/create")
    public RegisterResp addRegister(@RequestBody @Valid RegisterReq addRegister) {
        String registerType = addRegister.getRegistryTypeCode();
        Optional<Product> product = registerService.findProduct(addRegister.getInstanceId());

        if (product.isPresent()) {
            Optional<String> registerTypeDuplicate = registerService.checkDuplicateRegister(addRegister.getInstanceId(), addRegister.getRegistryTypeCode());
            if (registerTypeDuplicate.isPresent()) {
                throw new DuplicateRegisterException("У продукта c ID " + addRegister.getInstanceId() + " уже есть регистр с типом " + registerTypeDuplicate.get());
            }
        } else {
            throw new ProductNotFoundException("Для регистра не найден продукт с ID " + addRegister.getInstanceId());
        }

        Optional<String> checkRegisterType = registerService.checkRegisterType(registerType);

        if (checkRegisterType.isPresent()) {
            throw new RegisterNotFountException(checkRegisterType.get());
        }

        return registerService.addRegister(addRegister);
    }
}