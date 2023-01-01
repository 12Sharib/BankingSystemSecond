package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import com.cognologix.BankingSystem.dto.AccountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@Log4j2
class AccountServicesTest {
    @MockBean
    private AccountRepo accountRepo;
    @Autowired
    private AccountServiceImpl accountService;
    @MockBean
    private TransactionsRepository transactionsRepository;
    @Nested
    class SingleAccount{
        @BeforeEach
        void init(){
            Account account = new Account();
            account.setAccountNumber(101);

            when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number"));
            when(accountRepo.findById(account.getAccountNumber())).thenReturn(Optional.of(account));

        }
        @Test
        @DisplayName("positive singleAccount")
        void singleAccount() throws InvalidAccountNumber {
            log.info("Start positive singleAccount...");
            AccountDTO accountDTO = accountService.singleAccount(101);
            Assertions.assertEquals(101, accountDTO.getAccountNumber());
            log.info("end...");
        }
        @Test
        @DisplayName("negative, Invalid account number")
        void negative_singleAccount(){
            log.info("Start negative singleAccount...");
           Assertions.assertThrows(InvalidAccountNumber.class,
                   ()->accountService.singleAccount(-1));
           log.info("end...");
        }
    }
    @Nested
    class AllAccounts{
        @BeforeEach
        void init(){
            Account firstAccount = new Account(2, 21, "Sharib Saifi",
                    "Savings", 600.0, "Active", null);

            Account secondAccount = new Account(2, 21, "Sharib Saifi",
                    "Savings", 600.0, "Active", null);

            List<Account> accounts = List.of(firstAccount,secondAccount);
            when(accountRepo.findAll()).thenReturn(accounts);
        }
        @Test
        @DisplayName("positive allAccounts")
        void allAccounts(){
            log.info("Start positive allAccounts...");
            Assertions.assertEquals(2, accountService.allAccount().size());
            log.info("end...");

        }
        @Test
        @DisplayName("negative allAccounts, when list is empty")
        void negative_allAccounts(){
          log.info("Start negative allAccounts...");
            when(accountRepo.findAll()).thenReturn(List.of());
            Assertions.assertEquals(true, accountService.allAccount().isEmpty());
            log.info("end...");
        }
    }
    @Nested
    class AccountsWithSameId{
        @BeforeEach
        void init(){
            Account firstAccount = new Account(5,21,"Sharib Saifi","Savings",
                    600.0,"Active",null);

            Account secondAccount = new Account(6,21,"Sharib","current",
                    600.0,"Active",null);

            List<Account> accountList = List.of(firstAccount,secondAccount);

            when(accountRepo.existsByCustomerId(21)).thenReturn(true)
                    .thenThrow(new InvalidCustomerId("Invalid Customer Id"));
            when(accountRepo.findAllByCustomerId(21)).thenReturn(accountList);

        }
        @Test
        @DisplayName("positive accountsWithSameId")
        void accountsWithSameId() throws InvalidCustomerId{
            log.info("Start positive accountsWithSameId...");
            Assertions.assertEquals(2,accountService.sameId(21).size());
            log.info("end...");
        }
        @Test
        @DisplayName("negative accountsWithSameId, invalidCustomerId")
        void negative_accountsWithSameId(){
            log.info("Start negative accountsWithSameId...");
            Assertions.assertThrows(InvalidCustomerId.class,
                    ()-> accountService.sameId(5));
            log.info("end...");
        }
    }

    @Nested
    class DeleteAccount{
        @BeforeEach
        void init(){
            Account account = new Account();
            account.setAccountNumber(101);
            account.setAccountInitialBalance(0.0);

            Optional<Account> prevAccount = Optional.of(account);
            when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Delete"));
            when(accountRepo.findById(101)).thenReturn(prevAccount)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Delete Account"));

        }
        @Test
        @DisplayName("positive deleteAccount")
        void deleteAccount() throws InvalidAccountNumber{
            log.info("Start positive deleteAccount...");
            SuccessResponse result =  accountService.deleteAccount(101);
            Assertions.assertEquals(true,result.getSuccess());
            log.info("end...");

        }
        @Test
        @DisplayName("negative deleteAccount, invalidAccountNumber")
        void negative_deleteAccount(){
            log.info("Start negative deleteAccount...");
           Assertions.assertThrows(InvalidAccountNumber.class,
                   ()->accountService.deleteAccount(-9));
           log.info("end...");
        }
    }

    @Test
    void deleteAll(){
        log.info("Start deleteAll...");
        SuccessResponse response = accountService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
        log.info("end...");
    }
    @Nested
    class SavingsAccount{
        @BeforeEach
        void init() {
            Account account = new Account();

            account.setAccountType("savings");
            account.setAccountName("Sharib");
            List<Account> accountList = new ArrayList<>();
            accountList.add(account);

            when(accountRepo.findByAccountType("savings")).thenReturn(accountList);
        }
        @Test
        @DisplayName("positive savingsAccount")
        void p_savingsAccounts(){
            log.info("Start positive savingsAccount...");
                Assertions.assertEquals(1, accountService.savingsAccounts().size());
            log.info("end...");
        }
        @Test
        @DisplayName("negative savingsAccount, invalidAccountType")
        void n_savingsAccount(){
            log.info("start negative savingsAccount...");
            Assertions.assertThrows(AccountsNotExist.class,
                    ()->accountService.currentAccounts().size());
            log.info("end...");
        }
    }


    @Nested
    class CurrentsAccount{
        @BeforeEach
        void init() {
            Account account = new Account();

            account.setAccountType("current");
            account.setAccountName("Sharib");
            List<Account> accountList = new ArrayList<>();
            accountList.add(account);

            when(accountRepo.findByAccountType("current")).thenReturn(accountList);
        }
        @Test
        @DisplayName("positive currentAccount")
        void p_savingsAccounts(){
            log.info("Start positive currentAccount...");
                Assertions.assertEquals(1, accountService.currentAccounts().size());
            log.info("end...");
        }
        @Test
        @DisplayName("negative currentAccount, invalidAccountType")
        void n_savingsAccount(){
            log.info("Start negative currentAccount...");
            Assertions.assertThrows(AccountsNotExist.class,
                    ()->accountService.savingsAccounts().size());
            log.info("end...");
        }
    }

    @Test
    void accountCreation() throws InvalidAccountNumber{
        try {
            Account account = new Account();
            account.setAccountNumber(101);
            account.setAccountInitialBalance(500.0);
            account.setAccountType("savings...");
            account.setAccountName("Sharib...");

            when(accountRepo.findById(101)).thenReturn(Optional.of(account));
            Assertions.assertEquals("Sharib", accountRepo.findById(101).get().getAccountName());
        }catch (Exception exception) {
            System.out.println(exception.getMessage());
            Assertions.assertTrue(exception instanceof InvalidAccountNumber);
        }

    }

    @Nested
    class CreditCard{
        @BeforeEach
        void init(){
            Account account = new Account();
            account.setAccountNumber(20);
            account.setAccountInitialBalance(500.0);
            account.setAccountName("Sharib Saifi...");

            Optional<Account> prevAccount = Optional.of(account);

            when(accountRepo.existsById(20)).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Credit Card"));
            when(accountRepo.findById(20)).thenReturn(prevAccount);

        }
        @Test
        @DisplayName("positive, creditCard")
        void creditCard(){
            log.info("Start positive creditCard...");
                List<String> creditCard = accountService.creditCard(20);
                Assertions.assertEquals("Credit Card", creditCard.get(4));
            log.info("end...");
        }
        @Test
        @DisplayName("negative creditCard, NotEligibleForCreditCard")
        void notEligible(){
            log.info("start negative creditCard, NotEligibleForCreditCard...");
           Assertions.assertThrows(NotEligibleForCreditCard.class,
                   ()-> accountService.creditCard(20));
           log.info("end...");
        }
        @Test
        @DisplayName("negative, Invalid account Number")
        void invalidAccountNumber(){
            log.info("start negative creditCard, InvalidAccountNumber...");
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()-> accountService.creditCard(25));
           log.info("end...");
        }
    }

    @Nested
    class DebitCard{
        @BeforeEach
        void init(){
            Account account = new Account();
            account.setAccountNumber(20);
            account.setAccountInitialBalance(500.0);
            account.setAccountName("Sharib Saifi");

            Optional<Account> prevAccount = Optional.of(account);

            when(accountRepo.existsById(20)).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Credit Card"));
            when(accountRepo.findById(20)).thenReturn(prevAccount);

        }
        @Test
        @DisplayName("positive, debitCard")
        void debitCard() {
         log.info("Start positive debitCard...");
                List<String> debitCard = accountService.debitCard(20);
                Assertions.assertEquals("Debit Card", debitCard.get(4));
        log.info("end...");

        }
        @Test
        @DisplayName("negative debitCard, InvalidAccountNumber")
        void invalidAccountNumber(){
            log.info("Start negative debitcard, InvalidAccountNumber...");
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()-> accountService.debitCard(25));
            log.info("end...");
        }
    }
}

