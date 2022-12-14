package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidAmount;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {


    List<Transactions> oneAccountTransactions(Integer accountNumber) throws InvalidAccountNumber;

    Transactions transactionId(Integer transactionId) throws InvalidTransactionId;

    SuccessResponse deleteTransaction(Integer transactionId) throws InvalidTransactionId;

    List byDate(String date);

    List previousFive(Integer accountNumber) throws InvalidAccountNumber;

    TransactionDTO withdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber, InsufficientBalance, InvalidAmount;

    TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber, InvalidAmount;
    TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws InvalidAccountNumber,InsufficientBalance,InvalidAmount;
}
