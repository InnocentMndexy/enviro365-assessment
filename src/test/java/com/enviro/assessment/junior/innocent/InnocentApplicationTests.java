package com.enviro.assessment.junior.innocent;

import com.enviro.assessment.junior.innocent.dto.WithdrawalRequest;
import com.enviro.assessment.junior.innocent.model.*;
import com.enviro.assessment.junior.innocent.repository.*;
import com.enviro.assessment.junior.innocent.service.InvestorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InnocentApplicationTests {

    @Mock
    InvestorRepository investorRepo;

    @Mock
    InvestmentProductRepository productRepo;

    @Mock
    WithdrawalNoticeRepository withdrawalRepo;

    @InjectMocks
    InvestorService service;

    @Test
    void shouldRejectRetirementWithdrawalIfAgeLessThan65() {
        // Arrange
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setAge(55);

        InvestmentProduct product = new InvestmentProduct();
        product.setId(1L);
        product.setType("RETIREMENT");
        product.setBalance(100000.0);

        when(investorRepo.findById(1L))
            .thenReturn(Optional.of(investor));
        when(productRepo.findById(1L))
            .thenReturn(Optional.of(product));

        WithdrawalRequest req = new WithdrawalRequest();
        req.setInvestorId(1L);
        req.setProductId(1L);
        req.setAmount(10000.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.createWithdrawal(req));
    }

    @Test
    void shouldRejectWithdrawalExceeding90Percent() {
        // Arrange
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setAge(70);

        InvestmentProduct product = new InvestmentProduct();
        product.setId(1L);
        product.setType("RETIREMENT");
        product.setBalance(100000.0);

        when(investorRepo.findById(1L))
            .thenReturn(Optional.of(investor));
        when(productRepo.findById(1L))
            .thenReturn(Optional.of(product));

        WithdrawalRequest req = new WithdrawalRequest();
        req.setInvestorId(1L);
        req.setProductId(1L);
        req.setAmount(95000.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.createWithdrawal(req));
    }

    @Test
    void shouldRejectWithdrawalExceedingBalance() {
        // Arrange
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setAge(70);

        InvestmentProduct product = new InvestmentProduct();
        product.setId(1L);
        product.setType("RETIREMENT");
        product.setBalance(100000.0);

        when(investorRepo.findById(1L))
            .thenReturn(Optional.of(investor));
        when(productRepo.findById(1L))
            .thenReturn(Optional.of(product));

        WithdrawalRequest req = new WithdrawalRequest();
        req.setInvestorId(1L);
        req.setProductId(1L);
        req.setAmount(150000.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.createWithdrawal(req));
    }

    @Test
    void shouldThrowExceptionWhenInvestorNotFound() {
        // Arrange
        when(investorRepo.findById(99L))
            .thenReturn(Optional.empty());

        WithdrawalRequest req = new WithdrawalRequest();
        req.setInvestorId(99L);
        req.setProductId(1L);
        req.setAmount(1000.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> service.createWithdrawal(req));
    }
}