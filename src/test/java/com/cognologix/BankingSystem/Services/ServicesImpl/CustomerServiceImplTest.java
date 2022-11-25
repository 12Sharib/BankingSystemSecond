package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class CustomerServiceImplTest {
    @Autowired
    Customer customer;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void createCustomer() {
        customer.setCustomerId(101);
        Boolean result = customerRepository.existsById(101);
        Assertions.assertEquals(true,result);
    }
    @Test
    void getAllCustomer() {
       List<Customer> result = customerRepository.findAll();
       Assertions.assertEquals(2,result.size());
    }
}