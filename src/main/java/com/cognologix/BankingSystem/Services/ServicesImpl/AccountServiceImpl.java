package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Enums.Account.AccountType;
import com.cognologix.BankingSystem.Enums.Error.ErrorMessages;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
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
        log.info("Started method..");
        //all saving account with customer details
        List<Account> allSavingsAccounts = (List<Account>) accountRepo.findByAccountType(AccountType.SAVINGS.name());
        //but return only account
        List<AccountDTO> accountList = new ArrayList<>();
        if (allSavingsAccounts.isEmpty()) {
            log.error("savingsAccounts list is empty");
            throw new AccountsNotExist(ErrorMessages.ACCOUNT_NOT_EXIST.getErrorMessage());
        }
        else {
            allSavingsAccounts.forEach(
                    account -> {
                        accountList.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
        }
        log.info("Completed method..");
        return accountList;
    }
    /*
    * current Accounts
     */
    @Override
    public List currentAccounts() throws AccountsNotExist {
        log.info("Started method..");
        //all current accounts with customer
        List<Account> allCurrentAccounts = (List<Account>) accountRepo.findByAccountType(AccountType.CURRENT.name());
        //but return only account details
        List<AccountDTO> accountList = new ArrayList<>();
        if (allCurrentAccounts.isEmpty()) {
            log.error("currentAccount list is empty");
            throw new AccountsNotExist(ErrorMessages.ACCOUNT_NOT_EXIST.getErrorMessage());
        }
        else {
            allCurrentAccounts.forEach(
                    account -> {
                        accountList.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
        }
        log.info("Completed method..");
        return accountList;
    }
    /*
    * debit card
     */
    @Override
    public List<String> debitCard(Integer accountNumber) throws InvalidAccountNumber {
        log.info("Started method..");
        List<String> debitCard = new ArrayList<>();
        if (accountRepo.existsById(accountNumber)) {
            debitCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
            debitCard.add("Card Number: 1234 5264 8597");
            debitCard.add("Date: 03/22 To 04/26");
            debitCard.add("CVV: 233");
            debitCard.add("Debit Card");

            log.info("Completed method..");
            return debitCard;
        } else
            log.error("Invalid account number for Debit Card: " + accountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
    }
    /*
    * credit card
     */
    @Override
    public List<String> creditCard(Integer accountNumber) throws NotEligibleForCreditCard {
        log.info("Started method..");
        List<String> creditCard = new ArrayList<>();
        if (accountRepo.existsById(accountNumber)) {
            if (accountRepo.findById(accountNumber).get().getAccountInitialBalance() > 2000) {
                try {
                    creditCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
                    creditCard.add("Card Number: 1934 5244 8597");
                    creditCard.add("Date: 03/22 To 04/27");
                    creditCard.add("CVV: 202");
                    creditCard.add("Credit Card");
                    log.info("Completed method..");
                }catch (Exception exception){
                    log.fatal("Unwanted exception: " + exception.getMessage());
                }
                return creditCard;
            } else
                log.error("Not eligible for credit card, Insufficient balance");
            throw new NotEligibleForCreditCard(ErrorMessages.NOT_ELIGIBLE_FOR_CREDITCARD.getErrorMessage() + accountNumber);
        } else {
            log.error("Invalid account number for credit card: " + accountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
        }

    }
    /*
    * * all accounts
     */
    @Override
    public List<AccountDTO> allAccount() {
        log.info("Started method..");
        List<AccountDTO> allAccounts = new ArrayList<>();
        try {
            accountRepo.findAll().forEach(
                    account -> {
                        allAccounts.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
        }catch (Exception exception){
            log.fatal("Unwanted exception: " + exception.getMessage());
        }
        log.info("Completed method..");
        return allAccounts;
    }

    /*
    * delete single account
     */
    @Override
    public SuccessResponse deleteAccount(Integer accountNumber) throws InvalidAccountNumber,InsufficientBalance {
        log.info("Started method..");
        if (accountRepo.existsById(accountNumber)) {
            Account account = accountRepo.findById(accountNumber).get();
            if (account.getAccountInitialBalance() > 0) {
                log.error("Sufficient balance in account: " + account.getAccountInitialBalance());
                throw new InsufficientBalance(ErrorMessages.INSUFFICIENT_BALANCE.getErrorMessage() + account.getAccountInitialBalance());
            } else {
                try {
                    accountRepo.deleteById(accountNumber);
                    log.info("Completed method..");
                }catch (Exception exception) {
                    log.fatal("Unwanted exception: " + exception.getMessage());
                }
                return new SuccessResponse("Delete successfully", true);
            }
        } else {
            log.error("Invalid Account number: " +accountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
        }
    }
   /*
    * check how many accounts with same id;
    */
    @Override
    public List<AccountDTO> sameId(Integer customerId) {
        log.info("Started method..");
        List<AccountDTO> accountDTO = new ArrayList<>();
        if (accountRepo.existsByCustomerId(customerId)){
            accountRepo.findAllByCustomerId(customerId).forEach(
                    account -> {
                        accountDTO.add(AccountConvertor.convertEntityToDTO(account));
                    }
            );
            log.info("Completed method..");
            return accountDTO;
        }else
            log.error("Invalid customer id: " + customerId);
            throw new InvalidCustomerId(ErrorMessages.INVALID_CUSTOMER_ID.getErrorMessage() + customerId);
    }
    /*
    * delete all accounts
     */
    @Override
    public SuccessResponse deleteAll() {
        log.info("Started method..");
        accountRepo.deleteAll();
        log.info("Completed method..");
        return new SuccessResponse("Delete Successfully",true);
    }
    /*
    * account by account number
     */
    @Override
    public AccountDTO singleAccount(Integer accountNumber) {
        log.info("Started method..");
        AccountDTO accountDTO = new AccountDTO();
        if (accountRepo.existsById(accountNumber)){
            log.info("Completed method..");
            return AccountConvertor.convertEntityToDTO(accountRepo.findById(accountNumber).get());
        }else {
            log.error("Invalid Account Number");
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
        }
    }
}





