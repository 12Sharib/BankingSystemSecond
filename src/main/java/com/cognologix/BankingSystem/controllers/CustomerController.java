package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value = "/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> allCustomer = customerService.getAllCustomer();
        return new ResponseEntity<>(allCustomer, HttpStatus.OK);
    }

    /*
    * get customer with customerId in the database with account
     */
    @GetMapping(value = "/getCustomerById/{customerId}")
    public ResponseEntity<List<Customer>> getById(@PathVariable Integer customerId){
        List<Customer> sameIdCustomers = customerRepository.findByCustomerId(customerId);
        return new ResponseEntity<>(sameIdCustomers,HttpStatus.OK);
    }
     /*
    @PutMapping(value = "/updateCustomerDetails/{accountNumber}")
    public ResponseEntity<String> updateCustomerDetails(@RequestBody Customer updatedDetails, @PathVariable Integer accountNumber) throws InvalidAccountNumber {
        if (customerRepository.existsById(accountNumber)) {
            Customer updateCustomer = customerService.updateCustomerDetails(updatedDetails,accountNumber);
            return new ResponseEntity<>("customer Details Updated: \n" + updateCustomer,HttpStatus.OK);
        }else throw new InvalidAccountNumber("provide valid account number for update customer details");
    }
    */
}
