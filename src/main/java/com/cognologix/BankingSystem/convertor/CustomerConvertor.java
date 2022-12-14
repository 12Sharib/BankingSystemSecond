package com.cognologix.BankingSystem.convertor;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.dto.CustomerDTO;

public class CustomerConvertor {
    public static CustomerDTO entityToDto(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCutomerAccountType(),
                customer.getCustomerMobileNumber());
        return customerDTO;
    }
}
