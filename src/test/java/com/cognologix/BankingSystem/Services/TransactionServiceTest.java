package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.ServicesImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @MockBean
    TransactionsRepository transactionsRepository;
    @Autowired
    TransactionServiceImpl transactionService;

    @Test
    void getAllTransactions() throws Exception {

        Mockito.when(transactionsRepository.findAll()).thenReturn(Stream.of(new Transactions(1, 2,
                "03/02/2001",
                "10:24", 5000.0,
                "hello", 15000.0)).collect(Collectors.toList()));

        Assertions.assertEquals(1, transactionService.all().size());
    }

    @Test
    void getOneAccountTransaction() throws Exception {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(22);
        transactions.setAccountNumber(12);

        Mockito.when(transactionsRepository.findById(22)).thenReturn(Optional.of(transactions))
                .thenThrow(new InvalidAccountNumber("Invalid account number"));
    }

    @Test
    void findTransactionOnTransactionId() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(11);
        transactionsRepository.save(transactions);
        Mockito.when(transactionsRepository.existsById(1)).thenReturn(true).thenThrow(new InvalidAccountNumber("Invalid Transaction Id"));
    }

    @Test
    void deleteTransaction() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(10);
        transactions.setTransactionAmount(250.0);

        transactionsRepository.deleteById(10);
        Mockito.when(transactionsRepository.existsById(10)).thenReturn(false).thenThrow(new InvalidAccountNumber("Transaction Id exist"));
    }
}
