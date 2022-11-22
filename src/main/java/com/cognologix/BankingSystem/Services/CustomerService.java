package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Account createCustomer(Customer customer) throws InvalidDocument;

    Customer updateCustomerDetails(Customer updatedDetails,Integer accountNumber);

    Iterable<Customer> getAllCustomer();
}
