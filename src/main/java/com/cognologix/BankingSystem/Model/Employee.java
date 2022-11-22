package com.cognologix.BankingSystem.Model;

import lombok.Data;

@Data
public class Employee {
    private Integer employeeId;
    private Integer branchId;
    private String employeeName;
    private String employeePosition;
    private String employeePhoneNumber;
    private String employeeEmail;
    private String employeeAddress;
    private Integer employeeLogInId;
    private String employeeLogInIdPassword;
}
