package com.cognologix.BankingSystem.dto;

import com.cognologix.BankingSystem.Model.Account;
import lombok.Data;

@Data
public class AccountDTO {
    private Integer accountNumber;
    private Integer customerId;
    private String accountName;
    private String accountType;
    private Double accountInitialBalance;
    private String accountStatus;

}
