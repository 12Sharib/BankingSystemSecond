package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Model.Account;
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
    TransactionsRepository transactionsRepository;
    @Autowired
    TransactionServiceImpl transactionService;

    @Test
    void allTransactions(){
        when(transactionsRepository.findAll()).thenReturn(Stream.of(new Transactions(1, 2,
                "03/02/2001",
                "10:24", 5000.0,
                "hello", 15000.0)).collect(Collectors.toList()));

        Assertions.assertEquals(1, transactionService.all().size());
    }

    @Test
    void getOneAccountTransaction() throws Exception {
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
    void findTransactionOnTransactionId() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(11);

        Optional<Transactions> transactionsOp = Optional.of(transactions);

        when(transactionsRepository.findById(11)).thenReturn(transactionsOp);
        Assertions.assertEquals(11,transactionService.findTransactionOnTransactionId(11).getTransactionId());
    }

    @Test
    void deleteTransaction() {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(10);
        transactions.setTransactionAmount(250.0);

        when(transactionsRepository.findById(2)).thenReturn(Optional.of(transactions));
        when(transactionsRepository.existsById(transactions.getTransactionId())).thenReturn(true);

        String result = transactionService.deleteOneTransaction(transactions.getTransactionId());
        Assertions.assertEquals(" Delete Successfully ", result);
    }
}
