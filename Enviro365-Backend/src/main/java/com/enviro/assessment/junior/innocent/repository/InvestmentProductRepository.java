package com.enviro.assessment.junior.innocent.repository;

import com.enviro.assessment.junior.innocent.model.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentProductRepository 
    extends JpaRepository<InvestmentProduct, Long> {
}