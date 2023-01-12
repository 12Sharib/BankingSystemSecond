package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.LoanDTO;

public interface LoanService {
     SuccessResponse eligible(Integer creditScore);

     SuccessResponse request(Integer creditScore, Integer accountNumber, String loanType, Double amount);

    LoanDTO activate(Integer loanNumber);

    SuccessResponse status(Integer loanNumber);

    LoanDTO details(Integer loanNumber);

    SuccessResponse updateDetails(Loan loanDetails, Integer loanNumber);

    SuccessResponse delete(Integer loanNumber);
}
