package com.cognologix.BankingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Integer transactionId;
    private String transactionDate;
    private String transactionTime;
    private Double transactionAmount;
    private String transactionMessage;
    private Integer accountNumber;

    public TransactionDTO(Integer transactionId, String transactionDate, Double transactionAmount, String transactionMessage, String transactionTime,Integer accountNumber) {
        this.transactionId=transactionId;
        this.transactionDate=transactionDate;
        this.transactionAmount=transactionAmount;
        this.transactionMessage=transactionMessage;
        this.transactionTime=transactionTime;
        this.accountNumber=accountNumber;
    }
}
