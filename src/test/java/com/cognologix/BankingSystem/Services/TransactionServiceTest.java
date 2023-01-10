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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Log4j2
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
        @DisplayName("positive amountTransfer")
        void amountTransfer(){
            log.info("Start positive amountTransfer...");
                TransactionDTO transactionDTO = transactionService.transferAmount(1, 2, 300.0);
                Assertions.assertEquals("Money Transfer successfully", transactionDTO.getTransactionMessage());
            log.info("end...");
        }
        @Nested
        class negative_amountTransfer {
            @Test
            @DisplayName("when invalid first account Number")
            void invalidFirstAccount(){
                log.info("Start negative amountTransfer, invalidFirstAccountNumber...");
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.transferAmount(12, 2, 100.0));
                log.info("end...");
            }
            @Test
            @DisplayName("when invalid second account Number")
            void invalidSecondAccount(){
                log.info("Start negative amountTransfer, invalidSecondAccountNumber...");
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.transferAmount(1, 25, 100.0));
                log.info("end...");
            }
            @Test
            @DisplayName("when InvalidAmount")
            void invalidAmount(){
                log.info("Start negative amountTransfer, invalidAmount...");
                Assertions.assertThrows(InvalidAmount.class,
                        () -> transactionService.transferAmount(1, 2, -800.0));
                log.info("end...");
            }
            @Test
            @DisplayName("when Insufficient balance in sender's account")
            void insufficientBalance() {
                log.info("Start negative amountTransfer, insufficientBalance...");
                Assertions.assertThrows(InsufficientBalance.class,
                        () -> transactionService.transferAmount(1, 2, 800.0));
                log.info("end...");
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
        @DisplayName("positive depositAmount")
        void positive_deposit() {
            Double depositedAmount = 100.0;
            log.info("Start positive deposit...");
                TransactionDTO transactionDTO = transactionService.depositAmount(1, depositedAmount);
                Assertions.assertEquals(100.0, transactionDTO.getTransactionAmount());
            log.info("end...");
        }

        @Nested
        @DisplayName("negative Deposit")
        class negative_deposit {
            @Test
            @DisplayName("when InvalidAccountNumber")
            void invalidAccount() {
                log.info("Start negative deposit, invalidAccountNumber...");
                Assertions.assertThrows(InvalidAccountNumber.class,
                        () -> transactionService.depositAmount(2, 500.0));
                log.info("end...");
            }

            @Test
            @DisplayName("when Invalid Amount")
            void invalidAmount() {
                log.info("Start negative deposit, invalidAmount...");
                Assertions.assertThrows(InvalidAmount.class,
                        () -> transactionService.depositAmount(1, -500.0));
                log.info("end...");
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
        void withdraw() {
            //account
            log.info("Start positive withdraw...");
                Double withdrawAmount = 100.0;
                TransactionDTO transactionDTO = transactionService.withdrawAmount(1, withdrawAmount);
                Assertions.assertEquals(100.0, transactionDTO.getTransactionAmount());
            log.info("end...");
        }

        @Nested
        @DisplayName("negatives withdraw amount")
        class negative_withraw{
            @Test
            @DisplayName("when invalidAccountNumber")
            void invalidAccount(){
                log.info("Start negative withdraw, invalidAccountNumber...");
                Assertions.assertThrows(InvalidAccountNumber.class,
                        ()->transactionService.withdrawAmount(51,50.0));
                log.info("end...");
            }
            @Test
            @DisplayName("when invalidAmount")
            void invalidAmount(){
                log.info("Start negative withdraw, invalidAmount...");
                Assertions.assertThrows(InvalidAmount.class,
                        ()->transactionService.withdrawAmount(1,-50.0));
                log.info("end...");
            }
            @Test
            @DisplayName("when Insufficient Balance in account")
            void insufficientBalance(){
                log.info("Start negative withdraw, insufficientBalance...");
                Assertions.assertThrows(InsufficientBalance.class,
                        ()->transactionService.withdrawAmount(1,5000.0));
                log.info("end...");
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
        @DisplayName("positive oneAccountTransactions")
        void p_oneAccountTransaction() {
            log.info("Start positive oneAccountTransaction...");
            Assertions.assertEquals(2, transactionService.oneAccountTransactions(12).size());
            log.info("end...");
        }
        @Test
        @DisplayName("negative, when invalid account number")
        void n_oneAccountTransaction(){
            log.info("Start negative oneAccountTransaction, invalidAccountNumber...");
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()->transactionService.oneAccountTransactions(5));
            log.info("end...");
        }
    }
    @Nested
    class FindByTransactionId{
        @BeforeEach
        void init(){
            Transactions transactions = new Transactions();
            transactions.setTransactionId(11);

            Optional<Transactions> transactionsOp = Optional.of(transactions);

            when(transactionsRepository.existsById(transactions.getTransactionId())).thenReturn(true);
            when(transactionsRepository.findById(11)).thenReturn(transactionsOp);
        }
        @Test
        @DisplayName("positive findByTransactionId")
        void p_transactionId(){
            log.info("Start positive findByTransactionId...");
            Assertions.assertEquals(11, transactionService.transactionId(11).getTransactionId());
            log.info("end...");
        }
        @Test
        @DisplayName("negative, InvalidTransactionId")
        void n_transactionId(){
            log.info("Start negative findByTransactionId, invalidTransactionId...");
            Assertions.assertThrows(InvalidTransactionId.class,
                    ()->transactionService.transactionId(5));
            log.info("end...");
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
        @DisplayName("positive deleteTransaction")
        void p_deleteTransaction() throws InvalidTransactionId{
            log.info("Start positive deleteTransaction...");
            SuccessResponse result = transactionService.deleteTransaction(10);
            Assertions.assertEquals("Delete Successfully", result.getMessage());
            log.info("end...");
        }
        @Test
        @DisplayName("negative, invalidTransactionId")
        void n_deleteTransaction(){
            log.info("Start negative deleteTransaction, invalidTransactionId...");
            Assertions.assertThrows(InvalidTransactionId.class,
                    ()->transactionService.deleteTransaction(82));
            log.info("end...");
        }
    }
    @Test
    void byDate(){
        log.info("Start byDate...");
        String date = "02/02/20";
        Transactions firstTransactions = new Transactions();
        firstTransactions.setTransactionDate("02/02/20...");

        Transactions secondTransactions = new Transactions();
        secondTransactions.setTransactionDate("02/02/20...");

        List<Transactions> transactionsList = List.of(firstTransactions,secondTransactions);

        when(transactionsRepository.findByTransactionDate(date)).thenReturn(transactionsList);
        List<Transactions> result = transactionService.byDate(date);
        Assertions.assertEquals(2,result.size());
        log.info("end...");
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
        @DisplayName("positive previousFive")
        void p_previousFive() throws InvalidAccountNumber{
            log.info("Start positive previousFive...");
            List<Transactions> fiveTransactions = transactionService.previousFive(2);
            Assertions.assertEquals(1,fiveTransactions.size());
            log.info("end...");
        }
        @Test
        @DisplayName("negative, InvalidAccountNumber")
        void n_previousFive(){
            log.info("Start negative previousFive, invalidAccountNumber...");
            Assertions.assertThrows(InvalidAccountNumber.class,
                    ()->transactionService.previousFive(8));
            log.info("end...");
        }
    }
}
