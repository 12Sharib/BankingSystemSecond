package com.cognologix.BankingSystem.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Component
@Data
@Entity
@Table
public class Transactions {
    @Id
    private Integer transactionId;
    private String transactionDate;
    private String transactionTime;
    private Double transactionAmount;
    private String transactionMessage;
    private String fromAccountNumber;
    private String toAccountNumber;

}
