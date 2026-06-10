package com.enviro.assessment.junior.innocent.service;

import com.enviro.assessment.junior.innocent.dto.*;
import com.enviro.assessment.junior.innocent.model.*;
import com.enviro.assessment.junior.innocent.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestmentProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalRepository;

    public InvestorPortfolioDTO getPortfolio(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Investor not found"));

        InvestorPortfolioDTO dto = new InvestorPortfolioDTO();
        dto.setId(investor.getId());
        dto.setFirstName(investor.getFirstName());
        dto.setLastName(investor.getLastName());
        dto.setEmail(investor.getEmail());
        dto.setAge(investor.getAge());

        List<InvestorPortfolioDTO.ProductDTO> products = 
            investor.getProducts().stream().map(p -> {
                InvestorPortfolioDTO.ProductDTO pdto = 
                    new InvestorPortfolioDTO.ProductDTO();
                pdto.setId(p.getId());
                pdto.setName(p.getName());
                pdto.setType(p.getType());
                pdto.setBalance(p.getBalance());
                return pdto;
            }).collect(Collectors.toList());

        dto.setProducts(products);
        return dto;
    }

    @Transactional
    public WithdrawalNotice createWithdrawal(WithdrawalRequest request) {
        Investor investor = investorRepository.findById(
            request.getInvestorId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Investor not found"));

        InvestmentProduct product = productRepository.findById(
            request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Product not found"));

        if ("RETIREMENT".equalsIgnoreCase(product.getType()) 
                && investor.getAge() <= 65) {
            throw new IllegalArgumentException(
                "Retirement withdrawals only allowed for investors older than 65");
        }

        if (request.getAmount() > product.getBalance()) {
            throw new IllegalArgumentException(
                "Withdrawal amount exceeds available balance");
        }

        double maxAllowed = product.getBalance() * 0.90;
        if (request.getAmount() > maxAllowed) {
            throw new IllegalArgumentException(
                "Withdrawal amount cannot exceed 90% of balance (max: R"
                + String.format("%.2f", maxAllowed) + ")");
        }

        product.setBalance(product.getBalance() - request.getAmount());
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice();
        notice.setInvestorId(request.getInvestorId());
        notice.setProductId(request.getProductId());
        notice.setAmount(request.getAmount());
        notice.setWithdrawalDate(LocalDateTime.now());
        notice.setStatus("APPROVED");

        return withdrawalRepository.save(notice);
    }

    public List<WithdrawalNotice> getWithdrawals(Long investorId) {
        return withdrawalRepository.findByInvestorId(investorId);
    }
}