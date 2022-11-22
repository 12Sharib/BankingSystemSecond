package com.cognologix.BankingSystem.Model;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Loan {
    private Integer accountNumber;
    private Integer loanId;
    private String loanType;
    private Double loanAmount;
}
