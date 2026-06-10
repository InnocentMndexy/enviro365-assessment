package com.enviro.assessment.junior.innocent.controller;

import com.enviro.assessment.junior.innocent.dto.*;
import com.enviro.assessment.junior.innocent.model.WithdrawalNotice;
import com.enviro.assessment.junior.innocent.service.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;

    @GetMapping("/investors/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioDTO> getPortfolio(
            @PathVariable Long id) {
        return ResponseEntity.ok(investorService.getPortfolio(id));
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<WithdrawalNotice> createWithdrawal(
            @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(
            investorService.createWithdrawal(request));
    }

    @GetMapping("/withdrawals")
    public ResponseEntity<List<WithdrawalNotice>> getWithdrawals(
            @RequestParam Long investorId) {
        return ResponseEntity.ok(
            investorService.getWithdrawals(investorId));
    }
}