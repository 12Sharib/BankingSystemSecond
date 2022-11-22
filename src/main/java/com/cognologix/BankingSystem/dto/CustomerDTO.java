package com.cognologix.BankingSystem.dto;

import com.cognologix.BankingSystem.Model.Customer;
import lombok.Data;

@Data
public class CustomerDTO {
//    private Integer customerAccountNumber;
//    private String customerName;
//    private String accountType;
//    private Double accountInitialBalance;
//    private Integer accountCustomerId;
    private Customer customer;
}
