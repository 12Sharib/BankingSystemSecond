package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.dto.AccountDTO;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void transferAmount() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/account/transferAmount/1/2/23.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("provide valid first account Number")))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));

    }

    @Test
    void depositAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/account/deposit/6/23.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionMessage", is("Deposit amount")))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void withdrawAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/withdraw/20"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void allAccounts() throws Exception {
        AccountDTO accountDto= AccountDTO.builder()
                .accountNumber(1)
                .accountType("Savings")
                .accountStatus("Active")
                .accountInitialBalance(500.0)
                .customerId(12)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.get("/account/allAccounts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$[0].accountNumber",is(accountDto.getAccountNumber())))
                .andExpect(jsonPath("$[0].accountType",is("Savings")))
                .andReturn();
    }

    @Test
    void deleteAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/account/delete/24"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void findSavingAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/savings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType",is("Savings")))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void findCurrentAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/current"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("Does not have current accounts")))
                .andExpect(jsonPath("$.success",is(false)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void debitCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/debitCard/6"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$[4]",is("Debit Card")))
                .andReturn();
    }

    @Test
    void creditCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/creditCard/6"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.message",is("Balance Less than 2000, Not Eligible for Credit Card")))
                .andExpect(jsonPath("$.success",is(false)))
                .andReturn();
    }

    @Test
    void accountInOneId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/findAccountsInCustomerId/6"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.message",is("Invalid Customer Id")))
                .andExpect(jsonPath("$.success",is(false)))
                .andReturn();

    }

    @Test
    void deleteAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/withdraw/20"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Deleted Succesfully"))
                .andReturn();
    }
}