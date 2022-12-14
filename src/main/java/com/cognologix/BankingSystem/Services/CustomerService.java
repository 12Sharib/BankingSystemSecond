package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    Customer updateCustomerDetails(Customer updatedDetails,Integer accountNumber) throws InvalidAccountNumber;
    List<CustomerDTO> allCustomer();
    AccountDTO createCustomer(Customer customer);
    SuccessResponse deleteAll();
    List<CustomerDTO> customer(Integer customerId) throws InvalidCustomerId;
}
