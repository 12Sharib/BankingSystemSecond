package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import com.cognologix.BankingSystem.dto.TransactionDTO;
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
    void amountTransfer(){
        Account firstAccount = new Account();
        firstAccount.setAccountNumber(1);
        firstAccount.setAccountInitialBalance(500.0);

        Account secondAccount = new Account();
        secondAccount.setAccountNumber(2);
        secondAccount.setAccountInitialBalance(0.0);

        Optional<Account> firstAccountOp = Optional.of(firstAccount);
        Optional<Account> secondAccountOp = Optional.of(secondAccount);

        Transactions firstTransaction = new Transactions();
        firstTransaction.setTransactionId(1);
        firstTransaction.setTransactionMessage("withdraw");

        Transactions secondTransaction = new Transactions();
        secondTransaction.setTransactionId(1);
        secondTransaction.setTransactionMessage("deposit");


        when(accountRepo.findById(1)).thenReturn(firstAccountOp);
        when(accountRepo.save(firstAccount)).thenReturn(firstAccount);
        when(transactionsRepository.save(firstTransaction)).thenReturn(firstTransaction);

        when(accountRepo.findById(2)).thenReturn(secondAccountOp);
        when(accountRepo.save(secondAccount)).thenReturn(secondAccount);
        when(transactionsRepository.save(secondTransaction)).thenReturn(secondTransaction);

        TransactionDTO transactionDTO = accountService.transferAmount(1,2,300.0);
        Assertions.assertEquals("Money Transfer successfully",transactionDTO.getTransactionMessage());

    }
    @Test
    void deposit(){
        //account
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountInitialBalance(500.0);

        Optional<Account> prevAccount = Optional.of(account);
        //transaction in account
        Transactions transactions = new Transactions(12,1,"03/02/02","12:22",100.0,"withdraw",500.0);

        Double depositedAmount =100.0;
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        when(transactionsRepository.save(transactions)).thenReturn(transactions);

        TransactionDTO transactionDTO = accountService.depositAmount(1,depositedAmount);
        Assertions.assertEquals(100.0,transactionDTO.getTransactionAmount());

    }
    @Test
    void withdraw(){
        //account
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountInitialBalance(500.0);

        Optional<Account> prevAccount = Optional.of(account);

        //transaction in account
        Transactions transactions = new Transactions(12,1,"03/02/02","12:22",100.0,"withdraw",500.0);

        Double withdrawAmount =100.0;
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        when(transactionsRepository.save(transactions)).thenReturn(transactions);

        TransactionDTO transactionDTO = accountService.withdrawAmount(1,withdrawAmount);
        Assertions.assertEquals(100.0,transactionDTO.getTransactionAmount());

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
    void allAccountsInOneId(){
        Account firstAccount = new Account(5,21,"Sharib Saifi","Savings",
                600.0,"Active",null);

        Account secondAccount = new Account(6,21,"Sharib","current",
                600.0,"Active",null);

        List<Account> accountList = new ArrayList<>();
        accountList.add(firstAccount);
        accountList.add(secondAccount);

        when(accountRepo.existsByCustomerId(21)).thenReturn(true);
        when(accountRepo.findAllByCustomerId(21)).thenReturn(accountList);

        Assertions.assertEquals(2,accountService.accountsInSameId(21).size());
    }

    @Test
    void deleteAccount() {
        Account account = new Account();
        account.setAccountNumber(101);

        Optional<Account> prevAccount = Optional.of(account);
        when(accountRepo.findById(2)).thenReturn(prevAccount);
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);

        Boolean result =  accountService.deleteAccount(account.getAccountNumber());
        Assertions.assertEquals(true,result);

    }
    @Test
    void savingsAccounts() {
        Account account = new Account();

        account.setAccountType("saving");
        account.setAccountName("Sharib");
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountRepo.findAll()).thenReturn(accountList);
        Assertions.assertEquals(1,accountService.currentAccounts().size());
    }

    @Test
    void currentAccounts() {
        Account account = new Account();

        account.setAccountType("current");
        account.setAccountName("Sharib");
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountRepo.findAll()).thenReturn(accountList);
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

}

