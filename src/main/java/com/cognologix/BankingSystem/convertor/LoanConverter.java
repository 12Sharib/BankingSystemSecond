package com.cognologix.BankingSystem.convertor;

import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.dto.LoanDTO;

public class LoanConverter {
    public static LoanDTO convertLoanEntityToDto(Loan loan){
        return new LoanDTO(
                loan.getLoanNumber(),
                loan.getLoanAccountName(),
                loan.getLoanType(),
                loan.getLoanAmount(),
                loan.getLoanStatus(),
                loan.getLoanEmiMonths()
        );
    }
}
