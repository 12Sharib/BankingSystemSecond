package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidAmount;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import com.cognologix.BankingSystem.Services.ServicesImpl.TransactionServiceImpl;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.NestedTestConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @MockBean
    private TransactionsRepository transactionsRepository;
    @Autowired
    private TransactionServiceImpl transactionService;
    @MockBean
    private AccountRepo accountRepo;
    @Autowired
    private AccountServiceImpl accountService;

    @Nested
    class AmountTransfer{
        @BeforeEach
        void init(){
            Account firstAccount = new Account();
            firstAccount.setAccountNumber(1);
            firstAccount.setAccountInitialBalance(500.0);

            Account secondAccount = new Account();
            secondAccount.setAccountNumber(2);
            secondAccount.setAccountInitialBalance(50.0);

            Optional<Account> firstAccountOp = Optional.of(firstAccount);
            Optional<Account> secondAccountOp = Optional.of(secondAccount);

            Transactions firstTransaction = new Transactions();
            firstTransaction.setTransactionId(1);
            firstTransaction.setTransactionMessage("withdraw");

            Transactions secondTransaction = new Transactions();
            secondTransaction.setTransactionId(1);
            secondTransaction.setTransactionMessage("deposit");
            //exist or not
            when(accountRepo.existsById(firstAccount.getAccountNumber())).thenReturn(true);
            when(accountRepo.existsById(secondAccount.getAccountNumber())).thenReturn(true);
            //first account
            when(accountRepo.findById(firstAccount.getAccountNumber())).thenReturn(firstAccountOp)
                    .thenThrow(new InvalidAccountNumber("Invalid First Account Number"));
            when(accountRepo.save(firstAccount)).thenReturn(firstAccount);
            //first transaction
            when(transactionsRepository.save(firstTransaction)).thenReturn(firstTransaction);

            //second account
            when(accountRepo.findById(secondAccount.getAccountNumber())).thenReturn(secondAccountOp)
                    .thenThrow(new InvalidAccountNumber("Invalid Second Account Number"));
            when(accountRepo.save(secondAccount)).thenReturn(secondAccount);
            //second transaction
            when(transactionsRepository.save(secondTransaction)).thenReturn(secondTransaction);
        }
        @Test
        @DisplayName("positive amount transfer")
        void amountTransfer() throws InvalidAmount,InsufficientBalance,InvalidAccountNumber{
            try {
                TransactionDTO transactionDTO = transactionService.transferAmount(1, 2, 300.0);
                Assertions.assertEquals("Money Transfer successfully", transactionDTO.getTransactionMessage());
            }catch (InvalidAmount exception){
                Assertions.assertTrue(exception instanceof InvalidAmount);
            }
            catch (InsufficientBalance exception){
                Assertions.assertTrue(exception instanceof InsufficientBalance);
            }
            catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        @Nested
        class negative_amountTransfer {
            //when invalid first account Number
            //when invalid second account Number
            //when inSufficient balance in sender's account
            //when invalid transfer amount
            @Test
            @DisplayName("when invalid first account Number")
            void invalidFirstAccount(){
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.transferAmount(12, 2, 100.0));
            }
            @Test
            @DisplayName("when invalid first account Number")
            void invalidSecondAccount(){
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.transferAmount(1, 25, 100.0));
            }
            @Test
            @DisplayName("when Invalid amount")
            void invalidAmount(){
                Assertions.assertThrows(InvalidAmount.class,
                        () -> transactionService.transferAmount(1, 2, -800.0));
            }
            @Test
            @DisplayName("when Insufficient balance in sender's account")
            void insufficientBalance() {
                Assertions.assertThrows(InsufficientBalance.class,
                        () -> transactionService.transferAmount(1, 2, 800.0));

            }

        }
    }

    @Nested
    class Deposit {
        @BeforeEach
        void init(){
            Account account = new Account();
            account.setAccountNumber(1);
            account.setAccountInitialBalance(500.0);

            Optional<Account> prevAccount = Optional.of(account);
            //transaction in account
            Transactions transactions = new Transactions(
                    12, 1, "03/02/02", "12:22",
                    100.0, "withdraw", 0, 0, 500.0);

            when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number for Deposit"));
            when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
            when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
            when(transactionsRepository.save(transactions)).thenReturn(transactions);

        }
        @Test
        @DisplayName("Positive deposit amount")
        void positive_deposit() throws InvalidAmount, InvalidAccountNumber {
            Double depositedAmount = 100.0;
            try {
                TransactionDTO transactionDTO = transactionService.depositAmount(1, depositedAmount);
                Assertions.assertEquals(100.0, transactionDTO.getTransactionAmount());
            } catch (InvalidAmount exception) {
                Assertions.assertTrue(exception instanceof InvalidAmount);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        @Nested
        @DisplayName("negative Deposit")
        class negative_deposit {
            @Test
            @DisplayName("when Invalid account Number")
            void invalidAccount() {
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.depositAmount(2, 500.0));
            }

            @Test
            @DisplayName("when Invalid Amount")
            void invalidAmount() {
                Assertions.assertThrows(InvalidAmount.class,
                        () -> transactionService.depositAmount(1, -500.0));
            }
        }
    }
    @Nested
    class Withdraw {

        @BeforeEach
        void init() {
            Account account = new Account();
            account.setAccountNumber(1);
            account.setAccountInitialBalance(500.0);

            Optional<Account> prevAccount = Optional.of(account);

            //transaction in account
            Transactions transactions = new Transactions(
                    12, 1, "03/02/02", "12:22",
                    100.0, "withdraw", 0, 0, 500.0);

            when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number"));
            when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
            when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
            when(transactionsRepository.save(transactions)).thenReturn(transactions);
        }
        @Test
        @DisplayName("positive withdraw amount")
        void withdraw() throws InsufficientBalance, InvalidAmount, InvalidAccountNumber {
            //account
            try {
                Double withdrawAmount = 100.0;
                TransactionDTO transactionDTO = transactionService.withdrawAmount(1, withdrawAmount);
                Assertions.assertEquals(100.0, transactionDTO.getTransactionAmount());
            } catch (InvalidAmount exception) {
                Assertions.assertTrue(exception instanceof InvalidAmount);
            } catch (InsufficientBalance exception) {
                Assertions.assertTrue(exception instanceof InsufficientBalance);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        @Nested
        @DisplayName("negatives withdraw amount")
        class negative_withraw{
            @Test
            @DisplayName("when invalid account number")
            void invalidAccount(){
                Assertions.assertThrows(InvalidAccountNumber.class,
                        ()->transactionService.withdrawAmount(51,50.0));
            }
            @Test
            @DisplayName("when invalid amount")
            void invalidAmount(){
                Assertions.assertThrows(InvalidAmount.class,
                        ()->transactionService.withdrawAmount(1,-50.0));
            }
            @Test
            @DisplayName("when Insufficient Balance in account")
            void insufficientBalance(){
                Assertions.assertThrows(InsufficientBalance.class,
                        ()->transactionService.withdrawAmount(1,5000.0));
            }
        }
    }

    @Nested
    class OneAccountTransaction{
        @BeforeEach
        void init(){
            Transactions firstTransactions = new Transactions();
            firstTransactions.setTransactionId(22);
            firstTransactions.setAccountNumber(12);

            Transactions secondTransactions = new Transactions();
            secondTransactions.setTransactionId(10);
            secondTransactions.setAccountNumber(12);

            List<Transactions> transactionsList = List.of(firstTransactions,secondTransactions);

            when(transactionsRepository.existsByAccountNumber(12)).thenReturn(true);
            when(transactionsRepository.findByAccountNumber(12)).thenReturn(transactionsList);
        }
        @Test
        @DisplayName("positive one account transactions")
        void p_oneAccountTransaction() throws InvalidAccountNumber {
            try {
                Assertions.assertEquals(2, transactionService.oneAccountTransactions(12).size());
            }catch (InvalidAccountNumber accountNumber){
                Assertions.assertTrue(accountNumber instanceof InvalidAccountNumber);
            }catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when invalid account number")
        void n_oneAccountTransaction(){
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()->transactionService.oneAccountTransactions(5));
        }
    }
    @Nested
    class FindByTransactionId{
        @BeforeEach
        void init(){
            Transactions transactions = new Transactions();
            transactions.setTransactionId(11);

            Optional<Transactions> transactionsOp = Optional.of(transactions);

            when(transactionsRepository.findById(11)).thenReturn(transactionsOp);
        }
        @Test
        @DisplayName("positive find by transaction Id")
        void p_transactionId() throws InvalidTransactionId{
            try {
                Assertions.assertEquals(11, transactionService.transactionId(11).getTransactionId());
            }catch (InvalidTransactionId invalidTransactionId){
                Assertions.assertTrue(invalidTransactionId instanceof InvalidTransactionId);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative,when Invalid transaction Id")
        void n_transactionId(){
            Assertions.assertThrows(InvalidTransactionId.class,
                    ()->transactionService.transactionId(5));
        }
    }

    @Nested
    class DeleteTransaction{
        @BeforeEach
        void init(){
            Transactions transactions = new Transactions();
            transactions.setTransactionId(10);
            transactions.setTransactionAmount(250.0);

            when(transactionsRepository.existsById(transactions.getTransactionId())).thenReturn(true)
                    .thenThrow(new InvalidTransactionId("Invalid Transaction Id"));
            when(transactionsRepository.findById(2)).thenReturn(Optional.of(transactions));

        }
        @Test
        @DisplayName("positive delete transaction")
        void p_deleteTransaction() throws InvalidTransactionId{
            SuccessResponse result = transactionService.deleteTransaction(10);
            Assertions.assertEquals("Delete Successfully", result.getMessage());
        }
        @Test
        @DisplayName("negative, when invalid transaction id")
        void n_deleteTransaction(){
            Assertions.assertThrows(InvalidTransactionId.class,
                    ()->transactionService.deleteTransaction(82));
        }
    }
    @Test
    void byDate(){
        String date = "02/02/20";
        Transactions firstTransactions = new Transactions();
        firstTransactions.setTransactionDate("02/02/20");

        Transactions secondTransactions = new Transactions();
        secondTransactions.setTransactionDate("02/02/20");

        List<Transactions> transactionsList = List.of(firstTransactions,secondTransactions);

        when(transactionsRepository.findByTransactionDate(date)).thenReturn(transactionsList);
        List<Transactions> result = transactionService.byDate(date);
        Assertions.assertEquals(2,result.size());
    }
    @Nested
    class PreviousFive{
        @BeforeEach
        void init(){
            Transactions transactions = new Transactions();
            transactions.setTransactionId(101);
            transactions.setAccountNumber(2);

            List<Transactions> transactionsList = List.of(transactions);

            when(transactionsRepository.existsByAccountNumber(2)).thenReturn(true)
                    .thenThrow(new InvalidAccountNumber("Invalid Account Number"));
            when(transactionsRepository.previousFiveTransactions(2)).thenReturn(transactionsList);
        }
        @Test
        @DisplayName("positive previous five")
        void p_previousFive() throws InvalidAccountNumber{
            List<Transactions> fiveTransactions = transactionService.previousFive(2);
            Assertions.assertEquals(1,fiveTransactions.size());
        }
        @Test
        @DisplayName("negative, Invalid account number")
        void n_previousFive(){
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()->transactionService.previousFive(8));
        }
    }
}
