package com.enviro.assessment.junior.innocent.repository;

import com.enviro.assessment.junior.innocent.model.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WithdrawalNoticeRepository 
    extends JpaRepository<WithdrawalNotice, Long> {
    List<WithdrawalNotice> findByInvestorId(Long investorId);
    List<WithdrawalNotice> findByWithdrawalDateBetween(
        LocalDateTime start, LocalDateTime end);
}