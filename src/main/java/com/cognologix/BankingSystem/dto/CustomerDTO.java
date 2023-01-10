package com.cognologix.BankingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Integer customerId;
    private String customerName;
    private String customerAccountType;
    private String customerMobileNumber;

}
