package com.cognologix.BankingSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    private Integer transactionId;
    @NotNull
    private Integer accountNumber;
    @NotBlank
    private String transactionDate;
    @NotBlank
    private String transactionTime;
    @NotNull
    private Double transactionAmount;
    @NotBlank
    private String transactionMessage;

    private Integer transactionToAccount;
    private Integer transactionFromAccount;

    @NotNull
    private Double totalBalance;



}
