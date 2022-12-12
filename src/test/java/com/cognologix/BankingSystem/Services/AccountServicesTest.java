package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountServicesTest {
    @MockBean
    private AccountRepo accountRepo;
    @Autowired
    private AccountServiceImpl accountService;
    @MockBean
    private TransactionsRepository transactionsRepository;

    @Test
    void singleAccount(){
        Account account = new Account();
        account.setAccountNumber(101);

        when(accountRepo.existsById(101)).thenReturn(true);
        when(accountRepo.findById(101)).thenReturn(Optional.of(account));

        Assertions.assertEquals(101,accountService.singleAccount(101).get().getAccountNumber());
    }

    @Test
    void allAccounts() throws Exception{
        Account firstAccount = new Account(2,21,"Sharib Saifi",
                "Savings", 600.0,"Active",null);

        Account secondAccount = new Account(2,21,"Sharib Saifi",
                "Savings", 600.0,"Active",null);

        List<Account> accounts = new ArrayList<>();
        accounts.add(firstAccount);
        accounts.add(secondAccount);

        when(accountRepo.findAll()).thenReturn(accounts);
        Assertions.assertEquals(2, accountService.allAccount().size());


    }
    @Test
    void accountsWithSameId(){
        Account firstAccount = new Account(5,21,"Sharib Saifi","Savings",
                600.0,"Active",null);

        Account secondAccount = new Account(6,21,"Sharib","current",
                600.0,"Active",null);

        List<Account> accountList = new ArrayList<>();
        accountList.add(firstAccount);
        accountList.add(secondAccount);

        when(accountRepo.existsByCustomerId(21)).thenReturn(true);
        when(accountRepo.findAllByCustomerId(21)).thenReturn(accountList);

        Assertions.assertEquals(2,accountService.sameId(21).size());
    }

    @Test
    void deleteAccount() {
        Account account = new Account();
        account.setAccountNumber(101);
        account.setAccountInitialBalance(0.0);

        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.findById(101)).thenReturn(prevAccount);

        SuccessResponse result =  accountService.deleteAccount(account.getAccountNumber());
        Assertions.assertEquals(true,result.getSuccess());

    }
    @Test
    void deleteAll(){
        SuccessResponse response = accountService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
    }
    @Test
    void savingsAccounts() {
        Account account = new Account();

        account.setAccountType("saving");
        account.setAccountName("Sharib");
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountRepo.findByAccountType("savings")).thenReturn(accountList);
        Assertions.assertEquals(1,accountService.savingsAccounts().size());
    }

    @Test
    void currentAccounts() {
        Account account = new Account();

        account.setAccountType("current");
        account.setAccountName("Sharib");
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountRepo.findByAccountType("current")).thenReturn(accountList);
        Assertions.assertEquals(1,accountService.currentAccounts().size());
    }
    @Test
    void accountCreation(){
        Account account = new Account();
        account.setAccountNumber(101);
        account.setAccountInitialBalance(500.0);
        account.setAccountType("savings");
        account.setAccountName("Sharib");


        when(accountRepo.findById(101)).thenReturn(Optional.of(account));
        Assertions.assertEquals("Sharib",accountRepo.findById(101).get().getAccountName());
    }
    @Test
    void creditCard(){
        Account account = new Account();
        account.setAccountNumber(20);
        account.setAccountInitialBalance(5000.0);
        account.setAccountName("Sharib Saifi");

        Optional<Account> prevAccount = Optional.of(account);

        when(accountRepo.existsById(20)).thenReturn(true);
        when(accountRepo.findById(20)).thenReturn(prevAccount);

        List<String> creditCard = accountService.creditCard(20);
        Assertions.assertEquals("Credit Card",creditCard.get(4));
    }
    @Test
    void debitCard(){
        Account account = new Account();
        account.setAccountNumber(20);
        account.setAccountInitialBalance(500.0);
        account.setAccountName("Sharib Saifi");

        Optional<Account> prevAccount = Optional.of(account);

        when(accountRepo.existsById(20)).thenReturn(true);
        when(accountRepo.findById(20)).thenReturn(prevAccount);

        List<String> debitCard = accountService.debitCard(20);
        Assertions.assertEquals("Debit Card",debitCard.get(4));
    }

}

