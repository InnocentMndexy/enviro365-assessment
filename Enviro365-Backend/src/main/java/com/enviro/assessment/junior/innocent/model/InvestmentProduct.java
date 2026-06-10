package com.enviro.assessment.junior.innocent.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InvestmentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investor;
}