package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AccountControllerTest {
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void getAllAccounts() {
        List<Account>  accounts = accountRepo.findAll();
        assertEquals(2,accounts.size());
    }

    @Test
    void deleteAccount() {
      //  accountRepo.deleteById(46);
        customerRepository.deleteById(46);
      //  Boolean result = accountRepo.existsById(46);
        Boolean result = customerRepository.existsById(46);
        Assertions.assertEquals(false,result);
    }


}