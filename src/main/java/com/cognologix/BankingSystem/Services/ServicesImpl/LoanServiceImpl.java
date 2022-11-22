package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.Services.LoanService;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {
    @Override
    public Loan createLoan(Loan loan, Integer accountNumber) {
        return null;
    }
}
