package com.cognologix.BankingSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Loan {
    //for not existing customer in bank;
    @Id
    private Integer loanId;
    private Integer loanNumber;
    private String loanBranch = "The Bank";
    private Double loanAmount;
    private String loanType;
    private String loanAccountName;
    private Integer loanAccountNumber;
    private String loanStatus;
    private Integer loanEmiMonths;

}
