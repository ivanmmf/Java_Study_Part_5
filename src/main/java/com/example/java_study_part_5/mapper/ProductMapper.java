package com.example.java_study_part_5.mapper;
import com.example.java_study_part_5.api.ProductReq;
import com.example.java_study_part_5.models.Product;
import lombok.experimental.UtilityClass;

//@UtilityClass
public class ProductMapper {
    public static  Product toEntity(ProductReq req){
        Product product = new Product();
        product.setNumber(req.getContractNumber());
        product.setPriority(req.getPriority());
        product.setStartDateTime(req.getContractDate());
        product.setPenaltyRate(req.getInterestRatePenalty());
        product.setThresholdAmount(req.getThresholdAmount());
        product.setInterestRateType(req.getRateType());
        product.setTaxRate(req.getTaxPercentageRate());
        product.setInterestRateType(req.getRateType());
        return product;
    };
}
