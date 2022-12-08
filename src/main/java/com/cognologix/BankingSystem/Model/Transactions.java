package com.cognologix.BankingSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Component
@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    private Integer transactionId;
    private Integer accountNumber;
    private String transactionDate;
    private String transactionTime;
    private Double transactionAmount;
    private String transactionMessage;
    private Double totalBalance;



}
