package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
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
    @Nested
    class AllAccounts{
        List<AccountDTO> init(){
            AccountDTO account = new AccountDTO();
            account.setAccountNumber(1);
            account.setAccountInitialBalance(300.0);

            List<AccountDTO> accountList = List.of(account);
            when(accountService.allAccount()).thenReturn(accountList);
            return accountList;
        }
        @Test
        @DisplayName("positive accountList")
        void allAccounts() throws Exception {
           List<AccountDTO> accountList=init();
            mockMvc.perform(MockMvcRequestBuilders.get("/account/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(accountList)))
                    .andExpect(status().isFound())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accountList.size()))
                    .andReturn();
        }
        @Test
        @DisplayName("negative, when list is empty")
        void negative_allAccounts() throws Exception{
            mockMvc.perform(MockMvcRequestBuilders.get("/account/allAccounts")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
    }

    @Nested
    class DeleteAccount{
        SuccessResponse init() {
            Account account = new Account();
            account.setAccountNumber(1);

            SuccessResponse response = new SuccessResponse("delete Successfully", true);
            when(accountService.deleteAccount(account.getAccountNumber())).thenReturn(response);
            return response;
        }

        @Test
        @DisplayName("positive Delete Account")
        void p_deleteAccount() throws InvalidAccountNumber {
            try {
                SuccessResponse response = init();
                mockMvc.perform(MockMvcRequestBuilders.delete("/account/delete/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(response)))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(response.getMessage())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(response.getSuccess())))
                        .andReturn();
            }catch (InvalidAccountNumber accountNumber){
                Assertions.assertTrue(accountNumber instanceof InvalidAccountNumber);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }

        }
        @Test
        @DisplayName("negative, invalid account number")
        void negative_deleteAccount() throws Exception{
            SuccessResponse response = new SuccessResponse("Invalid Account Number",false);

            when(accountService.deleteAccount(-1)).thenReturn(response);
            mockMvc.perform(MockMvcRequestBuilders.delete("/account/delete/-1")
                            .content(objectMapper.writeValueAsString(response)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.success",is(false)))
                    .andReturn();
        }
    }
    @Nested
    class SavingsAccount{
        @Test
        @DisplayName("positive, savings account")
        void savingAccounts() throws AccountsNotExist {
            try {
                Account account = new Account();
                account.setAccountNumber(1);
                account.setAccountType("Savings");

                List<Account> accountList = List.of(account);

                when(accountService.savingsAccounts()).thenReturn(accountList);

                mockMvc.perform(MockMvcRequestBuilders.get("/account/savings")
                                .content(objectMapper.writeValueAsString(accountList)))
                        .andExpect(status().isFound())
                        .andExpect(jsonPath("$[0].accountType", is("Savings")))
                        .andExpect(jsonPath("$.size()").value(accountList.size()))
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andReturn();
            }catch (AccountsNotExist accountsNotExist){
                Assertions.assertTrue(accountsNotExist instanceof AccountsNotExist);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        @Test
        @DisplayName("negative, savings account")
        void negative_savingAccounts() throws Exception{
            when(accountService.savingsAccounts()).thenReturn(List.of());
            mockMvc.perform(MockMvcRequestBuilders.get("/account/savings")
                            .content(objectMapper.writeValueAsString(List.of())))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        }
    }

    @Nested
    class CurrentAccounts{
        @Test
        @DisplayName("positive, current account")
        void currentAccount() throws AccountsNotExist {
            try {
                Account account = new Account();
                account.setAccountNumber(1);
                account.setAccountType("current");

                List<Account> accountList = List.of(account);

                when(accountService.currentAccounts()).thenReturn(accountList);

                mockMvc.perform(MockMvcRequestBuilders.get("/account/current")
                                .content(objectMapper.writeValueAsString(accountList)))
                        .andExpect(status().isFound())
                        .andExpect(jsonPath("$[0].accountType", is("current")))
                        .andExpect(jsonPath("$.size()").value(accountList.size()))
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andReturn();
            }catch (AccountsNotExist accountsNotExist){
                Assertions.assertTrue(accountsNotExist instanceof AccountsNotExist);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when list is empty")
        void negative_currentAccounts() throws Exception{
            //when list of current accounts is empty
            when(accountService.currentAccounts()).thenReturn(List.of());
            mockMvc.perform(MockMvcRequestBuilders.get("/account/current")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
    }

    @Nested
    class DebitCard{
        @Test
        @DisplayName("positive, debit card")
        void debitCard() throws InvalidAccountNumber {
            try {
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
                        .andExpect(status().isCreated())
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("Name: " + account.getAccountName())))
                        .andExpect(jsonPath("$[4]", is("Debit Card")))
                        .andReturn();
            }catch (InvalidAccountNumber accountNumber){
                Assertions.assertTrue(accountNumber instanceof  InvalidAccountNumber);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, invalid account number")
        void negative_debitCard() throws Exception{
            //when debit card not created or invalid account number
            when(accountService.debitCard(-1)).thenReturn(List.of());

            mockMvc.perform(MockMvcRequestBuilders.get("/account/debitCard/-1")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
        }
    }
   @Nested
    class CreditCard{
       @Test
       @DisplayName("positive, credit card")
       void creditCard() throws InvalidAccountNumber {
           try {
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
                       .andExpect(status().isCreated())
                       .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                       .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("Name: " + account.getAccountName())))
                       .andExpect(jsonPath("$[4]", is("Credit Card")))
                       .andReturn();
           }catch (InvalidAccountNumber accountNumber){
               Assertions.assertTrue(accountNumber instanceof InvalidAccountNumber);
           }catch (Exception exception){
               throw new RuntimeException(exception.getMessage());
           }
       }
       @Test
       @DisplayName("negative, when invalid account number")
       void negative_creditCard() throws Exception{
           //when debit card not created or invalid account number
           when(accountService.creditCard(-1)).thenReturn(List.of());
           mockMvc.perform(MockMvcRequestBuilders.get("/account/creditCard/-1")
                           .content(objectMapper.writeValueAsString(null)))
                   .andExpect(MockMvcResultMatchers.status().isNoContent())
                   .andReturn();
       }
    }

    @Nested
    class AccountsWithSameId{
        @Test
        @DisplayName("positve, accounts with same id")
        void accountWithSameId() throws InvalidCustomerId {
            try {
                AccountDTO firstAccount = new AccountDTO();
                firstAccount.setAccountNumber(1);
                firstAccount.setCustomerId(12);

                AccountDTO secondAccount = new AccountDTO();
                secondAccount.setCustomerId(12);

                List<AccountDTO> accountList = List.of(firstAccount, secondAccount);

                when(accountService.sameId(firstAccount.getCustomerId())).thenReturn(accountList);

                mockMvc.perform(MockMvcRequestBuilders.get("/account/accountsWithSameId/12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(accountList)))
                        .andExpect(status().isFound())
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId", is(firstAccount.getCustomerId())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId", is(secondAccount.getCustomerId())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accountList.size()))
                        .andReturn();
            }catch (InvalidCustomerId invalidCustomerId){
                Assertions.assertTrue(invalidCustomerId instanceof InvalidCustomerId);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }

        }
        @Test
        @DisplayName("negative, invalid customer id")
        void negative_accountWithSameId() throws Exception{
            //when customer id is invalid aur list is empty
            when(accountService.sameId(-1)).thenReturn(List.of());
            mockMvc.perform(MockMvcRequestBuilders.get("/account/accountWithSameId/-1")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
    }

    @Nested
    class SingleAccount{
        @Test
        @DisplayName("positive, single account")
        void singleAccount() throws InvalidAccountNumber {
            try {
                AccountDTO account = new AccountDTO();
                account.setAccountNumber(1);
                account.setAccountName("Sharib Saifi");

                when(accountService.singleAccount(account.getAccountNumber())).thenReturn(account);

                mockMvc.perform(MockMvcRequestBuilders.get("/account/singleAccount/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(account)))
                        .andExpect(status().isFound())
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                        .andReturn();
            }catch (InvalidAccountNumber accountNumber){
                Assertions.assertTrue(accountNumber instanceof InvalidAccountNumber);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when invalid account number")
        void negative_singleAccount() throws Exception{
            //when invalid accountNumber
            when(accountService.singleAccount(-1)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.get("/account/singleAccount/")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
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

}