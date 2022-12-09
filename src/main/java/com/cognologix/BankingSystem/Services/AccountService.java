package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    TransactionDTO withdrawAmount(Integer AccountNumber, Double WithdrawAmount) throws InvalidAccountNumber,MinimumAccountBalance, AmountLessThanZero;

    TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber,AmountLessThanZero;

    List<Account> allAccount();

    Boolean deleteAccount(Integer accountNumber) throws InvalidAccountNumber;

    TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance;

    List savingsAccounts() throws NotPresentAnyAccount;

    List currentAccounts() throws NotPresentAnyAccount;

    List<String> debitCard(Integer accountNumber);
    List<String> creditCard(Integer accountNumber) throws NotEligibleForCreditCard;

    List<Account> accountsInSameId(Integer customerId);

    String deleteAll();

    Optional<Account> singleAccount(Integer accountNumber);
}

