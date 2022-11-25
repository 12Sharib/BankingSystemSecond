package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
  //  Account createCustomer(Customer customer) throws InvalidDocument;

    Customer updateCustomerDetails(Customer updatedDetails,Integer accountNumber);

    List<Customer> getAllCustomer();

    AccountDTO createCustomer(Customer customer);
}
