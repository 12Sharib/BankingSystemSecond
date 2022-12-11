package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    List<Account> allAccount();
    SuccessResponse deleteAccount(Integer accountNumber) throws InvalidAccountNumber;
    List savingsAccounts() throws NotPresentAnyAccount;
    List currentAccounts() throws NotPresentAnyAccount;
    List<String> debitCard(Integer accountNumber);
    List<String> creditCard(Integer accountNumber) throws NotEligibleForCreditCard;
    List<Account> accountsWithSameId(Integer customerId);
    SuccessResponse deleteAll();
    Optional<Account> singleAccount(Integer accountNumber);
}

