package com.enviro.assessment.junior.innocent.controller;

import com.enviro.assessment.junior.innocent.model.WithdrawalNotice;
import com.enviro.assessment.junior.innocent.service.InvestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CsvController {

    private final InvestorService investorService;

    @GetMapping("/withdrawals/export")
    public ResponseEntity<String> exportCsv(
            @RequestParam Long investorId) {
        List<WithdrawalNotice> notices = 
            investorService.getWithdrawals(investorId);

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Investor ID,Product ID,Amount,Date,Status\n");
        for (WithdrawalNotice n : notices) {
            csv.append(n.getId()).append(",")
               .append(n.getInvestorId()).append(",")
               .append(n.getProductId()).append(",")
               .append(n.getAmount()).append(",")
               .append(n.getWithdrawalDate()).append(",")
               .append(n.getStatus()).append("\n");
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=withdrawals.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csv.toString());
    }
}