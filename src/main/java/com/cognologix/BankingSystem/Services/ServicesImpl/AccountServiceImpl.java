package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.convertor.AccountConvertor;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Customer customer;
    @Autowired
    Transactions transactions;
    @Autowired
    TransactionsRepository transactionsRepository;
   /*
   * savings Accounts
    */
    @Override
    public List savingsAccounts() throws AccountsNotExist {
        //all saving account with customer details
        List<Account> allSavingsAccounts = (List<Account>) accountRepo.findByAccountType("savings");
        //but return only account
        List<AccountDTO> accountList = new ArrayList<>();
        if (allSavingsAccounts.isEmpty()) throw new AccountsNotExist("Does not have savings accounts");
        else {
            allSavingsAccounts.forEach(
                    account -> {
                        accountList.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
        }
        return accountList;
    }
    /*
    * current Accounts
     */
    @Override
    public List currentAccounts() throws AccountsNotExist {
        //all current accounts with customer
        List<Account> allSavingsAccounts = (List<Account>) accountRepo.findByAccountType("current");
        //but return only account details
        List<AccountDTO> accountList = new ArrayList<>();
        if (allSavingsAccounts.isEmpty()) throw new AccountsNotExist("Does not have current accounts");
        else {
            allSavingsAccounts.forEach(
                    account -> {
                        accountList.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
        }
        return accountList;
    }
    /*
    * debit card
     */
    @Override
    public List<String> debitCard(Integer accountNumber) throws InvalidAccountNumber {
        List<String> debitCard = new ArrayList<>();
        if (accountRepo.existsById(accountNumber)) {
            debitCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
            debitCard.add("Card Number: 1234 5264 8597");
            debitCard.add("Date: 03/22 To 04/26");
            debitCard.add("CVV: 233");
            debitCard.add("Debit Card");
            return debitCard;
        } else throw new InvalidAccountNumber("Invalid account number for Debit card");

    }
    /*
    * credit card
     */
    @Override
    public List<String> creditCard(Integer accountNumber) throws NotEligibleForCreditCard {
        List<String> creditCard = new ArrayList<>();
        if (accountRepo.existsById(accountNumber)) {
            if (accountRepo.findById(accountNumber).get().getAccountInitialBalance() > 2000) {
                creditCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
                creditCard.add("Card Number: 1934 5244 8597");
                creditCard.add("Date: 03/22 To 04/27");
                creditCard.add("CVV: 202");
                creditCard.add("Credit Card");
                return creditCard;
            } else throw new NotEligibleForCreditCard("Balance Less than 2000, Not Eligible for Credit Card");
        } else throw new InvalidAccountNumber("Invalid account number for credit card");
    }
    /*
    * all accounts
     */
    @Override
    public List<Account> allAccount() {
        List<Account> allAccounts = accountRepo.findAll();
        return allAccounts;
    }

    /*
    * delete single account
     */
    @Override
    public SuccessResponse deleteAccount(Integer accountNumber) throws InvalidAccountNumber,InsufficientBalance {
        if (accountRepo.existsById(accountNumber)) {
            Account account = accountRepo.findById(accountNumber).get();
            if (account.getAccountInitialBalance() > 0) {
                throw new InsufficientBalance("Account has some balance, Withdraw balance : " + account.getAccountInitialBalance());
            } else {
                accountRepo.deleteById(accountNumber);
                return new SuccessResponse("Delete successfully", true);
            }
        } else {
            return new SuccessResponse("Invalid Account Number", false);
        }
    }
   /*
    * check how many accounts with same id;
    */
    @Override
    public List<AccountDTO> sameId(Integer customerId) {
        List<AccountDTO> accountDTO = new ArrayList<>();
        if (accountRepo.existsByCustomerId(customerId)){
            accountRepo.findAllByCustomerId(customerId).forEach(
                    account -> {
                        accountDTO.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
            return accountDTO;
        }else throw new InvalidCustomerId("Invalid Customer Id");
    }
    /*
    * delete all accounts
     */
    @Override
    public SuccessResponse deleteAll() {
        accountRepo.deleteAll();
        return new SuccessResponse("Delete Successfully",true);
    }
    /*
    * account by account number
     */
    @Override
    public AccountDTO singleAccount(Integer accountNumber) {
        AccountDTO accountDTO = new AccountDTO();
        if (accountRepo.existsById(accountNumber)){
            return AccountConvertor.convertEntityToDTO(accountRepo.findById(accountNumber).get());

        }else {
            throw new InvalidAccountNumber("Invalid Account Number");
        }
    }
}





