package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.AccountDTO;
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
    public ResponseEntity<AccountDTO> createCustomer(@RequestBody Customer customer) {
        AccountDTO account = customerService.createCustomer(customer);
        return new ResponseEntity<>(account,HttpStatus.OK);
    }
    /*
    * get all customers
     */
    @GetMapping(value = "/allCustomers")
    public ResponseEntity<List<Customer>> allCustomers(){
        List<Customer> allCustomer = customerService.getAllCustomer();
        return new ResponseEntity<>(allCustomer, HttpStatus.OK);
    }

    /*
    * get customer with customerId in the database with account
     */
    @GetMapping(value = "/customerById/{customerId}")
    public ResponseEntity<List<Customer>> findById(@PathVariable Integer customerId){
        List<Customer> sameIdCustomers = customerRepository.findByCustomerId(customerId);
        return new ResponseEntity<>(sameIdCustomers,HttpStatus.OK);
    }
    /*
    * delete all customer
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll(){
        return new ResponseEntity<>(customerService.deleteAll(),HttpStatus.OK);
    }
    @PostMapping("updateCustomer/{accountNumber}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer,@PathVariable Integer accountNumber){
        return new ResponseEntity<>(customerService.updateCustomerDetails(updatedCustomer,accountNumber),HttpStatus.OK);
    }
}
