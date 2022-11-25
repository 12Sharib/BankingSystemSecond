package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.*;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
   // Account CreateAccount(Account account) ;
  //  Account CreateAccount(Account account);

   // Customer CreateAccount(account);

    TransactionDTO WithdrawAmount(Integer AccountNumber, Double WithdrawAmount) throws InvalidAccountNumber,MinimumAccountBalance, AmountLessThanZero;

    TransactionDTO DepositAmount(Integer AccountNumber, Double DepositedAmount) throws InvalidAccountNumber,AmountLessThanZero;

    Iterable<Account> GetAllAccount();

    Boolean DeleteAccount(Integer AccountNumber) throws InvalidAccountNumber;

    TransactionDTO transferOneToAnother(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance;

    List findSavingsAccounts() throws NotPresentAnyAccount;

    List findCurrentAccounts() throws NotPresentAnyAccount;

    List<String> getDebitCard(Integer accountNumber);
    List<String> getCreditCard(Integer accountNumber) throws NotEligibleForCreditCard;

    List<Account> findAccountInOneId(Integer customerId);
}

