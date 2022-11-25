package com.cognologix.BankingSystem.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private Integer transactionId;
    private String transactionDate;
    private String transactionTime;
    private Double transactionAmount;
    private String transactionMessage;
}
