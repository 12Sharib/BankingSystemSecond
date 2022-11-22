package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.*;
import com.cognologix.BankingSystem.Model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
   // Account CreateAccount(Account account) ;
  //  Account CreateAccount(Account account);

   // Customer CreateAccount(account);

    Account WithdrawAmount(Integer AccountNumber, Double WithdrawAmount) throws InvalidAccountNumber,MinimumAccountBalance, AmountLessThanZero;

    Account DepositAmount(Integer AccountNumber, Double DepositedAmount) throws InvalidAccountNumber,AmountLessThanZero;

    Iterable<Account> GetAllAccount();

    Boolean DeleteAccount(Integer AccountNumber) throws InvalidAccountNumber;

    List transferOneToAnother(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance;

    List findSavingsAccounts() throws NotPresentAnyAccount;

    List findCurrentAccounts() throws NotPresentAnyAccount;

    List<String> getDebitCard(Integer accountNumber);
    List<String> getCreditCard(Integer accountNumber) throws NotEligibleForCreditCard;

    List<Account> findAccountInOneId(Integer customerId);
}

