package com.enviro.assessment.junior.innocent.dto;

import lombok.Data;
import java.util.List;

@Data
public class InvestorPortfolioDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private List<ProductDTO> products;

    @Data
    public static class ProductDTO {
        private Long id;
        private String name;
        private String type;
        private Double balance;
    }
}