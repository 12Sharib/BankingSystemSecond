package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountServicesTest {
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private AccountServiceImpl accountService;

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
    void findById(){
        Account account = new Account();
        account.setAccountNumber(101);
        account.setAccountInitialBalance(500.0);

        when(accountRepo.existsById(101)).thenReturn(true).thenThrow(new InvalidAccountNumber("Invalid account Number"));
    }
    @Test
    void deposit(){
        doThrow(new InvalidAccountNumber("Invalid account Number"))
                .when(accountService).depositAmount(5,500.0);
    }
    @Test
    void withdraw(){
       doThrow(new InvalidAccountNumber("Invalid account Number"))
               .when(accountService).withdrawAmount(5,500.0);
    }
    @Test
    void allAccounts() throws Exception{
        Customer firstCustomer = new Customer(1,21,"Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com","8006590554",
                "1111 2222 3333","OGHPS2812E","Muradanagar");

        Mockito.when(accountRepo.findAll()).thenReturn(Stream.of(new Account(2,21,"Sharib Saifi",
                        "Savings", 600.0,"Active",firstCustomer))
                            .collect(Collectors.toList()));

        Assertions.assertEquals(1, accountService.allAccount().size());

    }
    @Test
    void allAccountsInOneId(){
        //failed
        Customer firstCustomer = new Customer(1,21,"Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com","8006590554",
                "1111 2222 3333","OGHPS2812E","Muradanagar");

        Customer secondCustomer = new Customer(6,25,"Sharib",
                "Current", "SharibSaifi.KS@gmail.com","8003590554",
                "1111 2202 3333","OGHPSOO12E","Muradanagar");

        Account firstAccount = new Account(5,21,"Sharib Saifi","Savings",
                600.0,"Active",firstCustomer);

        Account secondAccount = new Account(6,21,"Sharib","current",
                600.0,"Active",firstCustomer);

        List<Account> accountList = new ArrayList<>();
        accountList.add(firstAccount);
        accountList.add(secondAccount);

        Mockito.when(accountRepo.findAllByCustomerId(1)).thenReturn(accountList).thenThrow(new Throwable("No, customer was found with the same Id"));

        Assertions.assertEquals(0,accountService.accountsInSameId(1));
    }

    @Test
    void deleteAccount() {
        //passed case
        when(accountRepo.existsById(2)).thenReturn(false);

        //failed case
        doThrow(new Throwable("Invalid Account Number")).when(accountService).deleteAccount(2);
    }
    @Test
    void savingsAccounts() {
        Account account = new Account();
        account.setAccountNumber(101);
        account.setAccountInitialBalance(500.0);
        account.setAccountType("savings");
        account.setAccountName("Sharib");

        when(accountRepo.findById(101)).thenReturn(Optional.of(account));
        Assertions.assertEquals("savings",accountRepo.findById(101).get().getAccountType());
    }

    @Test
    void currentAccounts() {
        Account account = new Account();
        account.setAccountNumber(101);
        account.setAccountInitialBalance(500.0);
        account.setAccountType("current");
        account.setAccountName("Sharib");

        when(accountRepo.findById(101)).thenReturn(Optional.of(account));
        Assertions.assertEquals("current",accountRepo.findById(101).get().getAccountType());
    }

}

