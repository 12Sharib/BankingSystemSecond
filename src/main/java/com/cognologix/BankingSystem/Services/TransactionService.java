package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Transactions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {

    //all transactions
    List<Transactions> all();

    //transactions in one account number;
    List<Transactions> oneAccountTransactions(Integer accountNumber);

    Optional<Transactions> findTransactionOnTransactionId(Integer transactionId);

    String deleteOneTransaction(Integer transactionId);

    List byDate(String date);

    List previousFive(Integer accountNumber);

    String deleteAll();
}
