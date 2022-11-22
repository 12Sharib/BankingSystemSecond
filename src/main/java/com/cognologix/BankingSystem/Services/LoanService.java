package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Loan;
import org.springframework.stereotype.Service;

@Service
public interface LoanService {
    Loan createLoan(Loan loan, Integer accountNumber);
}
