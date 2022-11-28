package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Transactions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {

    //all transactions
    List<Transactions> getAllTransactions();

    //transactions in one account number;
    List<Transactions> getOneAccountTransaction(Integer accountNumber);

    Optional<Transactions> findTransactionOnTransactionId(Integer transactionId);
}
