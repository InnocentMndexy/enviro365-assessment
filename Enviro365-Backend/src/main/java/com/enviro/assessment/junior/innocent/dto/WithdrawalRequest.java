package com.enviro.assessment.junior.innocent.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class WithdrawalRequest {
    @NotNull(message = "Investor ID is required")
    private Long investorId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}