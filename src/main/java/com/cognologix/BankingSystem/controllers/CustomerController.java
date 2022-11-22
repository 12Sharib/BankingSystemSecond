package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
   
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
/*
    @PostMapping(value = "/createCustomer")
    public ResponseEntity<Account> createCustomer(@Valid @RequestBody Customer customer) throws InvalidDocument {
        Account newCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.OK);
    }

 */
    @PostMapping("/createCustomer")
    public Customer createCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerRepository.save(customerDTO.getCustomer());
        return customer;
    }
    @PutMapping(value = "/updateCustomerDetails/{accountNumber}")
    public ResponseEntity<String> updateCustomerDetails(@RequestBody Customer updatedDetails, @PathVariable Integer accountNumber) throws InvalidAccountNumber {
        if (customerRepository.existsById(accountNumber)) {
            Customer updateCustomer = customerService.updateCustomerDetails(updatedDetails,accountNumber);
            return new ResponseEntity<>("customer Details Updated: \n" + updateCustomer,HttpStatus.OK);
        }else throw new InvalidAccountNumber("provide valid account number for update customer details");
    }
    @GetMapping(value = "/getAllCustomers")
    public ResponseEntity<Iterable<Customer>> getAllCustomers(){
        Iterable<Customer> allCustomer = customerService.getAllCustomer();
        return new ResponseEntity<>(allCustomer, HttpStatus.OK);
    }
    @GetMapping(value = "/getCustomerById/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable Integer customerId){
        return new ResponseEntity<>(customerRepository.findById(customerId).get(),HttpStatus.OK);
    }


}
