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
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
@Log4j2
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;
    /*
    * create customer
     */
    @PostMapping("/createCustomer")
    public ResponseEntity<AccountDTO> createCustomer(@Valid @RequestBody Customer customer) throws InvalidDocument {
        log.info("Accessed Create Customer With Customer Details");
        AccountDTO account = customerService.createCustomer(customer);
        HttpStatus httpStatus = account==null?HttpStatus.NO_CONTENT: HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(account,httpStatus);
    }
    /*
    * get all customers
     */
    @GetMapping(value = "/allCustomers")
    public ResponseEntity<List<CustomerDTO>> allCustomers(){
        log.info("Accessed All Customer");
        List<CustomerDTO> allCustomer = customerService.allCustomer();
        HttpStatus httpStatus = allCustomer.isEmpty()?HttpStatus.NOT_FOUND: HttpStatus.FOUND;
        log.info("Completed All Customers: " + httpStatus);
        return new ResponseEntity<>(allCustomer, httpStatus);
    }
    /*
    * get customer with customerId in the database with account
     */
    @GetMapping(value = "/customerById/{customerId}")
    public ResponseEntity<List<CustomerDTO>> customer(@PathVariable Integer customerId) throws InvalidCustomerId {
        log.info("Accessed Single Customer With Customer ID");
        List<CustomerDTO> sameIdCustomers = customerService.customer(customerId);
        if (sameIdCustomers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else
            log.info("Completed");
            return new ResponseEntity<>(sameIdCustomers,HttpStatus.FOUND);
    }
    /*
    * delete all customer
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        log.info("Accessed DeleteAll");
        return new ResponseEntity<>(customerService.deleteAll(),HttpStatus.OK);
    }
    @PutMapping("updateCustomer/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@Valid @RequestBody Customer updatedCustomer,@PathVariable Integer customerId) throws InvalidCustomerId {
        log.info("Accessed Update Customer with Update Details & Customer Id");
        CustomerDTO customer = customerService.updateCustomerDetails(updatedCustomer,customerId);
        HttpStatus httpStatus = customer==null?HttpStatus.NOT_MODIFIED: HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(customer,httpStatus);
    }
}
