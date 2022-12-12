package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    List<Transactions> all();

    List<Transactions> oneAccountTransactions(Integer accountNumber);

    Transactions transactionId(Integer transactionId);

    SuccessResponse deleteTransaction(Integer transactionId);

    List byDate(String date);

    List previousFive(Integer accountNumber);

    SuccessResponse deleteAll();
    TransactionDTO withdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber, MinimumAccountBalance, AmountLessThanZero;

    TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber,AmountLessThanZero;
    TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance;
}
