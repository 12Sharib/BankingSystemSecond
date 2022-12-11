package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import com.cognologix.BankingSystem.Services.ServicesImpl.TransactionServiceImpl;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        TransactionDTO transactionDTO = transactionService.transferAmount(1,2,300.0);
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
        Transactions transactions = new Transactions(
                12,1,"03/02/02","12:22",
                100.0,"withdraw",0,0,500.0);

        Double depositedAmount =100.0;
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        when(transactionsRepository.save(transactions)).thenReturn(transactions);

        TransactionDTO transactionDTO = transactionService.depositAmount(1,depositedAmount);
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
        Transactions transactions = new Transactions(
                12,1,"03/02/02","12:22",
                100.0,"withdraw",0,0,500.0);

        Double withdrawAmount =100.0;
        when(accountRepo.existsById(account.getAccountNumber())).thenReturn(true);
        when(accountRepo.findById(account.getAccountNumber())).thenReturn(prevAccount);
        when(accountRepo.save(prevAccount.get())).thenReturn(prevAccount.get());
        when(transactionsRepository.save(transactions)).thenReturn(transactions);

        TransactionDTO transactionDTO = transactionService.withdrawAmount(1,withdrawAmount);
        Assertions.assertEquals(100.0,transactionDTO.getTransactionAmount());

    }
    @Test
    void allTransactions(){
        when(transactionsRepository.findAll()).thenReturn(Stream.of(new Transactions(1, 2,
                "03/02/2001",
                "10:24", 5000.0,
                "hello",0,0, 15000.0)).collect(Collectors.toList()));

        Assertions.assertEquals(1, transactionService.all().size());
    }

    @Test
    void oneAccountTransaction() throws Exception {
        List<Transactions>  transactionsList = new ArrayList<>();

        Transactions firstTransactions = new Transactions();
        firstTransactions.setTransactionId(22);
        firstTransactions.setAccountNumber(12);

        Transactions secondTransactions = new Transactions();
        secondTransactions.setTransactionId(10);
        secondTransactions.setAccountNumber(12);

        transactionsList.add(firstTransactions);
        transactionsList.add(secondTransactions);

        when(transactionsRepository.findByAccountNumber(12)).thenReturn(transactionsList);
        Assertions.assertEquals(2,transactionService.oneAccountTransactions(12).size());
    }

    @Test
    void findByTransactionId() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(11);

        Optional<Transactions> transactionsOp = Optional.of(transactions);

        when(transactionsRepository.findById(11)).thenReturn(transactionsOp);
        Assertions.assertEquals(11,transactionService.transactionId(11).getTransactionId());
    }

    @Test
    void deleteTransaction() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(10);
        transactions.setTransactionAmount(250.0);

        when(transactionsRepository.findById(2)).thenReturn(Optional.of(transactions));
        when(transactionsRepository.existsById(transactions.getTransactionId())).thenReturn(true);

        SuccessResponse result = transactionService.deleteTransaction(transactions.getTransactionId());
        Assertions.assertEquals("Delete Successfully", result.getMessage());
    }
    @Test
    void byDate(){
        String date = "02/02/20";
        Transactions firstTransactions = new Transactions();
        firstTransactions.setTransactionDate("02/02/20");

        Transactions secondTransactions = new Transactions();
        secondTransactions.setTransactionDate("02/02/20");

        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(firstTransactions);
        transactionsList.add(secondTransactions);

        when(transactionsRepository.findByTransactionDate(date)).thenReturn(transactionsList);
        List<Transactions> result = transactionService.byDate(date);
        Assertions.assertEquals(2,result.size());
    }
    @Test
    void previousFive(){
        Transactions transactions = new Transactions();
        transactions.setTransactionId(101);
        transactions.setAccountNumber(2);

        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(transactions);

        when(transactionsRepository.existsByAccountNumber(2)).thenReturn(true);
        when(transactionsRepository.previousFiveTransactions(2)).thenReturn(transactionsList);

       List<Transactions> fiveTransactions = transactionService.previousFive(2);
        Assertions.assertEquals(1,fiveTransactions.size());
    }
}
