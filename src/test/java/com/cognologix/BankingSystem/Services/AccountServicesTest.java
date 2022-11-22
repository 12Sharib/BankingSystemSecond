package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.controllers.AccountController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountServicesTest {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountController accountController;
    @Autowired
    private Account account;
    @Autowired
    private Customer customer;
    @Autowired
   private CustomerRepository customerRepository;
    @Test
    void ExitsById() {
        Boolean actualResult = accountRepo.existsById(44);
        assertThat(actualResult).isTrue();
    }
    @Test
    void accountCreation(){
        account.setAccountNumber(101);
        account.setAccountInitialBalance(500.0);
        account.setAccountType("savings");
        Account createOrNot = accountRepo.save(account);
        //assertThat(createOrNot).toString();
        Assertions.assertEquals(101,createOrNot.getAccountNumber());

    }
    @Test
    void findById(){
        Optional<Account> x = accountRepo.findById(101);
        assertThat(x).toString();
    }
    @Test
    void deposit(){
        Account account1 = accountRepo.findById(7).get();
        Customer customer1 = customerRepository.findById(7).get();

        Double intitialBalance = account1.getAccountInitialBalance() + 500.0;
        account1.setAccountInitialBalance(intitialBalance);
        customer1.setCustomerAccountBalance(intitialBalance);

        Account depositInAccount = accountRepo.save(account1);
        Customer depositInCustomer = customerRepository.save(customer1);

        Assertions.assertEquals(2700,depositInAccount.getAccountInitialBalance());
        Assertions.assertEquals(2700,depositInCustomer.getCustomerAccountBalance());


    }
    @Test
    void withdraw(){
        Account account1 = accountRepo.findById(7).get();
        Customer customer1 = customerRepository.findById(7).get();

        Double intitialBalance = account1.getAccountInitialBalance() - 500.0;
        account1.setAccountInitialBalance(intitialBalance);
        customer1.setCustomerAccountBalance(intitialBalance);

        Account depositInAccount = accountRepo.save(account1);
        Customer depositInCustomer = customerRepository.save(customer1);

        Assertions.assertEquals(2200,depositInAccount.getAccountInitialBalance());
        Assertions.assertEquals(2200,depositInCustomer.getCustomerAccountBalance());

    }
    @Test
    void allAccounts(){
        List<Account> account1 = accountRepo.findAll();
        Assertions.assertEquals(2,account1.size());
    }
    @Test
    void allAccountsInOneId(){
        List<Account> accountList = accountRepo.findAllByAccountCustomerId(2);
        Assertions.assertEquals(2,accountList.size());
    }
    @Test
    void getAllAccounts() {
        List<Account>  accounts = accountRepo.findAll();
        assertEquals(5,accounts.size());
    }

    @Test
    void deleteAccount() {
        accountRepo.deleteById(29);
        customerRepository.deleteById(29);
        //  Boolean result = accountRepo.existsById(46);
        Boolean result = customerRepository.existsById(29);
        Assertions.assertEquals(false,result);
    }
}

