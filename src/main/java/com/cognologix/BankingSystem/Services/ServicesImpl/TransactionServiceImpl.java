package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    Transactions transactions;
    @Autowired
    TransactionsRepository transactionsRepository;

    @Override
    public List<Transactions> all() {
        List<Transactions> transactionsList = transactionsRepository.findAll();
        return transactionsList;
    }

    @Override
    public List<Transactions> oneAccountTransactions(Integer accountNumber) {
        List<Transactions> transactionsList = transactionsRepository.findByAccountNumber(accountNumber);
        return transactionsList;

    }

    @Override
    public Optional<Transactions> findTransactionOnTransactionId(Integer transactionId) {
        return transactionsRepository.findById(transactionId);
    }

    @Override
    public String deleteOneTransaction(Integer transactionId) {
        String message = null;
        if(transactionsRepository.existsById(transactionId)){
            transactionsRepository.deleteById(transactionId);
            message =" Delete Successfully ";
        }else throw new InvalidAccountNumber("Invalid transactionId");
        return message;
    }

    @Override
    public List byDate(String date) {

        //String currentDate = String.valueOf(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        List<Transactions> transactionsList = transactionsRepository.findAll();
        List<Transactions> sameDataTransactions = new ArrayList<>();
        transactionsList.forEach(transaction ->
        {  if(transaction.getTransactionDate().equals(date)) sameDataTransactions.add(transaction); });
        return sameDataTransactions;
    }

    @Override
    public List previousFive(Integer accountNumber) {
        List<Transactions> transactionsList = transactionsRepository.findAll();
        List<Transactions> previousFive = new ArrayList<>();
        transactionsList.forEach(transaction ->
        {
            if (previousFive.size()<=5) previousFive.add(transaction);
        });

        return previousFive;
    }

    @Override
    public String deleteAll() {
        transactionsRepository.deleteAll();
        return "delete successfully";
    }


}
