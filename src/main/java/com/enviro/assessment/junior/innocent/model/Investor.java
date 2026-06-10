package com.enviro.assessment.junior.innocent.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private int age;

    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvestmentProduct> products;
}