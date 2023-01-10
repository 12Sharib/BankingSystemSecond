package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidAmount;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.TransactionService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TransactionController.class)
@Log4j2
class TransactionControllerTest {
    @MockBean
    TransactionService transactionService;
    @MockBean
    AccountRepo accountRepo;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class Withdraw{
        @Test
        @DisplayName("positive, withdraw")
        void p_withdraw() throws Exception {
            Account account = new Account();
            account.setAccountInitialBalance(500.0);
            account.setAccountNumber(1);

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionId(3);
            transactionDTO.setTransactionAmount(100.0);

            when(transactionService.withdrawAmount(account.getAccountNumber(), 100.0)).thenReturn(transactionDTO);

            mockMvc.perform(MockMvcRequestBuilders.put("/transactions/withdraw/1/100")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionDTO)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", is(transactionDTO.getTransactionId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactionAmount", is(transactionDTO.getTransactionAmount())))
                    .andExpect(status().isCreated())
                    .andReturn();
        }
        @Test
        @DisplayName("negative, when invalid account number")
        void negative_withdraw() throws Exception{
            //when account number is invalid
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionId(101);

            when(transactionService.withdrawAmount(0,5.0)).thenReturn(transactionDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/transaction/withdraw/0")
                            .content(objectMapper.writeValueAsString(transactionDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        }
    }

   @Nested
   class Deposit{
       @Test
       @DisplayName("positive, Deposit")
       void p_deposit() throws Exception{
               Account account = new Account();
               account.setAccountInitialBalance(500.0);
               account.setAccountNumber(1);

               TransactionDTO transactionDTO = new TransactionDTO();
               transactionDTO.setTransactionId(3);
               transactionDTO.setTransactionAmount(100.0);

               when(transactionService.depositAmount(account.getAccountNumber(), 100.0)).thenReturn(transactionDTO);

               mockMvc.perform(MockMvcRequestBuilders.put("/transactions/deposit/1/100")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(transactionDTO)))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", is(transactionDTO.getTransactionId())))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionAmount", is(transactionDTO.getTransactionAmount())))
                       .andExpect(status().isCreated())
                       .andReturn();
       }
       @Test
       @DisplayName("negative, when invalid account number")
       void negative_deposit() throws Exception{
           //when account number is invalid
           TransactionDTO transactionDTO = new TransactionDTO();
           transactionDTO.setTransactionId(101);

           when(transactionService.depositAmount(0,5.0)).thenReturn(transactionDTO);
           mockMvc.perform(MockMvcRequestBuilders.put("/transaction/deposit/0")
                           .content(objectMapper.writeValueAsString(transactionDTO)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound())
                   .andReturn();

       }
   }
    @Nested
    class TransferAmount{
       @Test
       @DisplayName("positive, transfer amount")
       void transferAmount() throws Exception {
               Account firstAccount = new Account();
               firstAccount.setAccountInitialBalance(500.0);
               firstAccount.setAccountNumber(1);

               Account secondAccount = new Account();
               secondAccount.setAccountInitialBalance(300.0);
               secondAccount.setAccountNumber(2);

               TransactionDTO transactionDTO = new TransactionDTO();
               transactionDTO.setTransactionId(3);
               transactionDTO.setTransactionAmount(100.0);
               transactionDTO.setTransactionMessage("Money Transfer Successfully");

               when(accountRepo.existsById(firstAccount.getAccountNumber())).thenReturn(true);
               when(accountRepo.existsById(secondAccount.getAccountNumber())).thenReturn(true);
               when(transactionService.transferAmount(firstAccount.getAccountNumber(), secondAccount.getAccountNumber(), 100.0)).thenReturn(transactionDTO);

               mockMvc.perform(MockMvcRequestBuilders.put("/transactions/transferAmount/1/2/100")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(transactionDTO)))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", is(transactionDTO.getTransactionId())))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionAmount", is(transactionDTO.getTransactionAmount())))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionMessage", is(transactionDTO.getTransactionMessage())))
                       .andExpect(status().isCreated())
                       .andReturn();
       }
        @Test
        @DisplayName("negative, when invalid account number")
        void negative_transfer() throws Exception{
            //when account number is invalid
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionId(101);

            when(transactionService.transferAmount(0,2,5.0)).thenReturn(transactionDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/transaction/transferAmount/0")
                            .content(objectMapper.writeValueAsString(transactionDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        }
   }
   @Nested
   class OneAccountTransaction{
       @Test
       @DisplayName("positive, one account transactions")
       void oneAccountTransactions() throws Exception{
               Transactions firstTransactions = new Transactions();
               firstTransactions.setTransactionId(1);
               firstTransactions.setAccountNumber(5);

               Transactions secondTransactions = new Transactions();
               secondTransactions.setTransactionId(2);
               secondTransactions.setAccountNumber(5);

               List<Transactions> transactionsList = new ArrayList<>();
               transactionsList.add(firstTransactions);
               transactionsList.add(secondTransactions);

               when(transactionService.oneAccountTransactions(5)).thenReturn(transactionsList);

               mockMvc.perform(MockMvcRequestBuilders.get("/transactions/oneAccountTransactions/5")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(transactionsList)))
                       .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber", is(firstTransactions.getAccountNumber())))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionsList.size()))
                       .andExpect(MockMvcResultMatchers.status().isFound());
       }
       @Test
       @DisplayName("negative, when transaction id is invalid")
       void negative_oneAccountTransactions() throws Exception{
           when(transactionService.oneAccountTransactions(2)).thenReturn(null);
           mockMvc.perform(MockMvcRequestBuilders.get("/transaction/oneAccountTransaction/1")
                           .content(objectMapper.writeValueAsString(null)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound())
                   .andReturn();
       }

   }

   @Nested
   class FindByTransactionId{
       @Test
       @DisplayName("positive, find by transaction id")
       void p_findByTransactionId() throws Exception{
               Transactions transactions = new Transactions();
               transactions.setTransactionId(101);
               transactions.setTransactionMessage("deposit");

               when(transactionService.transactionId(101)).thenReturn(transactions);
               mockMvc.perform(MockMvcRequestBuilders.get("/transactions/findByTransactionId/101")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(new ObjectMapper().writeValueAsString(transactions)))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", is(101)))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.transactionMessage", is("deposit")))
                       .andExpect(MockMvcResultMatchers.status().isFound())
                       .andReturn();

       }
       @Test
       @DisplayName("negative, when invalid transaction id")
       void n_findByTransactionId() throws Exception{
           when(transactionService.transactionId(5)).thenReturn(null);
           mockMvc.perform(MockMvcRequestBuilders.get("/transaction/findByTransactionId/1")
                           .content(objectMapper.writeValueAsString(null)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound())
                   .andReturn();
       }
   }

    @Nested
    class DeleteTransaction{
        @Test
        @DisplayName("positive delete transactions")
        void deleteTransaction() throws Exception{
                Transactions transactions = new Transactions();
                transactions.setTransactionId(1);

                SuccessResponse response = new SuccessResponse("Delete Successfully", true);
                when(transactionService.deleteTransaction(1)).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/deleteTransaction/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(response)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(response.getMessage())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        }

        @Test
        @DisplayName("negative, when invalid transaction id")
        void negative_deleteTransaction() throws Exception{
            when(transactionService.previousFive(1)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.get("/transactions/previousFive")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(status().isNotFound())
                    .andReturn();
        }
    }
    @Nested
    class PreviousFive{
        @Test
        @DisplayName("positive previous five")
        void previousFive() throws Exception {
                Transactions transactions = new Transactions();
                transactions.setTransactionId(1);
                transactions.setAccountNumber(5);

                List<Transactions> transactionsList = new ArrayList<>();
                transactionsList.add(transactions);

                when(transactionService.previousFive(5)).thenReturn(transactionsList);

                mockMvc.perform(MockMvcRequestBuilders.get("/transactions/previousFiveTransactions/5")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionsList)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionsList.size()))
                        .andExpect(status().isFound())
                        .andReturn();
        }
        @Test
        @DisplayName("negative, when invalid account number")
        void negative_previousFive() throws Exception{
            //when invalid account id
            when(transactionService.previousFive(1)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.get("/transactions/previousFive")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(status().isNotFound())
                    .andReturn();
        }

    }
    @Test
    void byDate() throws Exception{
        Transactions transactions = new Transactions();
        transactions.setTransactionId(1);
        transactions.setTransactionDate("02-02-2022");

        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(transactions);

        when(transactionService.byDate(transactions.getTransactionDate())).thenReturn(transactionsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/byDate/02-02-2022")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionsList)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionId",is(1)))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andReturn();
    }
}