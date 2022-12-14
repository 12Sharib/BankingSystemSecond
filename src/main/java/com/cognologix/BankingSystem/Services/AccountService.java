package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    List<Account> allAccount();
    SuccessResponse deleteAccount(Integer accountNumber) throws InvalidAccountNumber;
    List savingsAccounts() throws AccountsNotExist;
    List currentAccounts() throws AccountsNotExist;
    List<String> debitCard(Integer accountNumber) throws InvalidAccountNumber;
    List<String> creditCard(Integer accountNumber) throws NotEligibleForCreditCard;
    List<AccountDTO> sameId(Integer customerId);
    SuccessResponse deleteAll();
    AccountDTO singleAccount(Integer accountNumber) throws InvalidAccountNumber;
}

