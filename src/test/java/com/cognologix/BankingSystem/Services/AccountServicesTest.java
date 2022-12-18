package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import com.cognologix.BankingSystem.dto.AccountDTO;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
        @DisplayName("positive single account")
        void singleAccount() throws InvalidAccountNumber {
            AccountDTO accountDTO = accountService.singleAccount(101);
            Assertions.assertEquals(101, accountDTO.getAccountNumber());
        }
        @Test
        @DisplayName("negative, Invalid account number")
        void negative_singleAccount(){
           Assertions.assertThrows(InvalidAccountNumber.class,
                   ()->accountService.singleAccount(-1));
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
        @DisplayName("positive all accounts")
        void allAccounts() throws Exception{
            try {
                Assertions.assertEquals(2, accountService.allAccount().size());
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative all accounts")
        void negative_allAccounts(){
            //when account list is empty
            when(accountRepo.findAll()).thenReturn(List.of());
            Assertions.assertEquals(true, accountService.allAccount().isEmpty());
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
        @DisplayName("positive accounts with same id")
        void accountsWithSameId() throws InvalidCustomerId{
            Assertions.assertEquals(2,accountService.sameId(21).size());
        }
        @Test
        @DisplayName("negative, invalid customer id")
        void negative_accountsWithSameId(){
            //Invalid customer id
            Assertions.assertThrows(InvalidCustomerId.class,
                    ()-> accountService.sameId(5));
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
        @DisplayName("positive delete account")
        void deleteAccount() throws InvalidAccountNumber{
            SuccessResponse result =  accountService.deleteAccount(101);
            Assertions.assertEquals(true,result.getSuccess());

        }
        @Test
        @DisplayName("negative, invalid account number")
        void negative_deleteAccount(){
           Assertions.assertThrows(NoSuchElementException.class,
                   ()->accountService.deleteAccount(-9));
        }
    }

    @Test
    void deleteAll(){
        SuccessResponse response = accountService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
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
        @DisplayName("positive savings account")
        void p_savingsAccounts() throws AccountsNotExist{
            try {
                Assertions.assertEquals(1, accountService.savingsAccounts().size());
            }
            catch (AccountsNotExist exception){
                System.out.println(exception.getMessage());
                Assertions.assertTrue(exception instanceof AccountsNotExist);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when invalid account type")
        void n_savingsAccount(){
            Assertions.assertThrows(AccountsNotExist.class,
                    ()->accountService.currentAccounts().size());
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
        @DisplayName("positive current account")
        void p_savingsAccounts() throws AccountsNotExist{
            try {
                Assertions.assertEquals(1, accountService.currentAccounts().size());
            }
            catch (AccountsNotExist exception){
                System.out.println(exception.getMessage());
                Assertions.assertTrue(exception instanceof AccountsNotExist);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when invalid account type")
        void n_savingsAccount(){
            Assertions.assertThrows(AccountsNotExist.class,
                    ()->accountService.savingsAccounts().size());
        }
    }

    @Test
    void accountCreation() throws InvalidAccountNumber{
        try {
            Account account = new Account();
            account.setAccountNumber(101);
            account.setAccountInitialBalance(500.0);
            account.setAccountType("savings");
            account.setAccountName("Sharib");

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
            account.setAccountName("Sharib Saifi");

            Optional<Account> prevAccount = Optional.of(account);

            when(accountRepo.existsById(20)).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Credit Card"));
            when(accountRepo.findById(20)).thenReturn(prevAccount);

        }
        @Test
        @DisplayName("positive, credit card")
        void creditCard() throws InvalidAccountNumber,NotEligibleForCreditCard{
            try {

                List<String> creditCard = accountService.creditCard(20);
                Assertions.assertEquals("Credit Card", creditCard.get(4));
            }catch (Exception exception){
                System.out.println(exception.getMessage());
                Assertions.assertTrue(exception instanceof NotEligibleForCreditCard);
                Assertions.assertEquals("Balance Less than 2000, Not Eligible for Credit Card",exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, Not eligible for credit card")
        void notEligible(){
           Assertions.assertThrows(NotEligibleForCreditCard.class,
                   ()-> accountService.creditCard(20));
        }
        @Test
        @DisplayName("negative, Invalid account Number")
        void invalidAccountNumber(){
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()-> accountService.creditCard(25));
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
        @DisplayName("positive, debit card")
        void debitCard() throws InvalidAccountNumber{
            try {

                List<String> debitCard = accountService.debitCard(20);
                Assertions.assertEquals("Debit Card", debitCard.get(4));
            }catch (Exception exception){
                System.out.println(exception.getMessage());
                Assertions.assertTrue(exception instanceof NotEligibleForCreditCard);
                Assertions.assertEquals("Balance Less than 2000, Not Eligible for Credit Card",exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, Invalid account Number")
        void invalidAccountNumber(){
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()-> accountService.debitCard(25));
        }
    }

}

