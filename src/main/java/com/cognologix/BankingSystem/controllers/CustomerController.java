package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;
    /*
    * create customer
     */
    @PostMapping("/createCustomer")
    public ResponseEntity<AccountDTO> createCustomer(@RequestBody Customer customer) throws InvalidDocument {
        AccountDTO account = customerService.createCustomer(customer);
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    /*
    * get all customers
     */
    @GetMapping(value = "/allCustomers")
    public ResponseEntity<List<CustomerDTO>> allCustomers(){
        List<CustomerDTO> allCustomer = customerService.allCustomer();
        if (allCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else return new ResponseEntity<>(allCustomer, HttpStatus.FOUND);
    }
    /*
    * get customer with customerId in the database with account
     */
    @GetMapping(value = "/customerById/{customerId}")
    public ResponseEntity<List<CustomerDTO>> customer(@PathVariable Integer customerId) throws InvalidCustomerId {
        List<CustomerDTO> sameIdCustomers = customerService.customer(customerId);
        if (sameIdCustomers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else return new ResponseEntity<>(sameIdCustomers,HttpStatus.FOUND);
    }
    /*
    * delete all customer
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        return new ResponseEntity<>(customerService.deleteAll(),HttpStatus.OK);
    }
    @PostMapping("updateCustomer/{accountNumber}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer,@PathVariable Integer accountNumber) throws InvalidAccountNumber {
        return new ResponseEntity<>(customerService.updateCustomerDetails(updatedCustomer,accountNumber),HttpStatus.CREATED);
    }
}
