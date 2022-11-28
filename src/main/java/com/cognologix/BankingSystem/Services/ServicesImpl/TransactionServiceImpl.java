package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    Transactions transactions;
    @Autowired
    TransactionsRepository transactionsRepository;

    @Override
    public List<Transactions> getAllTransactions() {
        List<Transactions> transactionsList = transactionsRepository.findAll();
        return transactionsList;
    }

    @Override
    public List<Transactions> getOneAccountTransaction(Integer accountNumber) {
        List<Transactions> transactionsList = transactionsRepository.findByToAccountNumber(Integer.toString(accountNumber));
        return transactionsList;
    }

    @Override
    public Optional<Transactions> findTransactionOnTransactionId(Integer transactionId) {
        return transactionsRepository.findById(transactionId);
    }
}
