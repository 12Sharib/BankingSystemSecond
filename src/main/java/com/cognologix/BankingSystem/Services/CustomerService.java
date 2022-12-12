package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    Customer updateCustomerDetails(Customer updatedDetails,Integer accountNumber);
    List<Customer> allCustomer();
    AccountDTO createCustomer(Customer customer);
    SuccessResponse deleteAll();

    List<Customer> findById(Integer customerId);
}
