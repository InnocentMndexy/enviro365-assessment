package com.enviro.assessment.junior.innocent.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class WithdrawalNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long investorId;
    private Long productId;
    private Double amount;
    private LocalDateTime withdrawalDate;
    private String status;
}