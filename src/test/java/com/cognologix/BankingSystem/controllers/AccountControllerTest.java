package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepo accountRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void allAccounts() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountInitialBalance(300.0);

        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountService.allAccount()).thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/allAccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountList)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accountList.size()))
                .andReturn();
    }

    @Test
    void deleteAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);

        SuccessResponse response = new SuccessResponse("delete Successfully",true);
        when(accountService.deleteAccount(account.getAccountNumber())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",is(response.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",is(response.getSuccess())))
                .andReturn();
    }

    @Test
    void savingAccounts() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountType("Savings");

        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountService.savingsAccounts()).thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/savings")
                        .content(objectMapper.writeValueAsString(accountList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType",is("Savings")))
                .andExpect(jsonPath("$.size()").value(accountList.size()))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void currentAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountType("current");

        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        when(accountService.currentAccounts()).thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/current")
                        .content(objectMapper.writeValueAsString(accountList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType",is("current")))
                .andExpect(jsonPath("$.size()").value(accountList.size()))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void debitCard() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountName("Sharib saifi");

        List<String> debitCard = new ArrayList<>();
            debitCard.add("Name: " + account.getAccountName());
            debitCard.add("Card Number: 1234 5264 8597");
            debitCard.add("Date: 03/22 To 04/26");
            debitCard.add("CVV: 233");
            debitCard.add("Debit Card");

        when(accountService.debitCard(1)).thenReturn(debitCard);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/debitCard/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(debitCard)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",is("Name: " + account.getAccountName())))
                .andExpect(jsonPath("$[4]",is("Debit Card")))
                .andReturn();
    }

    @Test
    void creditCard() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountName("Sharib saifi");
        account.setAccountInitialBalance(3000.0);

        List<String> creditCard = new ArrayList<>();
        creditCard.add("Name: " + account.getAccountName());
        creditCard.add("Card Number: 1234 5264 8597");
        creditCard.add("Date: 03/22 To 04/26");
        creditCard.add("CVV: 233");
        creditCard.add("Credit Card");

        when(accountService.creditCard(1)).thenReturn(creditCard);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/creditCard/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",is("Name: " + account.getAccountName())))
                .andExpect(jsonPath("$[4]",is("Credit Card")))
                .andReturn();
    }

    @Test
    void accountWithSameId() throws Exception {
        Account firstAccount = new Account();
        firstAccount.setAccountNumber(1);
        firstAccount.setCustomerId(12);

        Account secondAccount = new Account();
        secondAccount.setCustomerId(12);

        List<Account> accountList = new ArrayList<>();
        accountList.add(firstAccount);
        accountList.add(secondAccount);

        when(accountService.sameId(firstAccount.getCustomerId())).thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/accountsWithSameId/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountList)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId",is(firstAccount.getCustomerId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId",is(secondAccount.getCustomerId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accountList.size()))
                .andReturn();

    }

    @Test
    void deleteAll() throws Exception {
        SuccessResponse response = new SuccessResponse("Delete Successfully",true);
        when(accountService.deleteAll()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/deleteAll")
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",is("Delete Successfully")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",is(true)))
                .andReturn();
    }
    @Test
    void singleAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1);
        account.setAccountName("Sharib Saifi");

        Optional<Account> prevAccount = Optional.of(account);
        when(accountService.singleAccount(1)).thenReturn(prevAccount);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/singleAccount/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prevAccount)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.accountNumber",is(account.getAccountNumber())))
                .andReturn();
    }
}