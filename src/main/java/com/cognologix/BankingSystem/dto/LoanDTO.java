package com.cognologix.BankingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    private Integer loanNumber;
    private String loanAccountName;
    private String loanType;
    private Double loanAmount;
    private String loanStatus;
    private Integer loanEmiMonths;
}
