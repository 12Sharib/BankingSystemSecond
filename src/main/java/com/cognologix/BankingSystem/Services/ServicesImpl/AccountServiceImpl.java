package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.*;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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

    Random random = new Random();
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
    /*
    * withdraw amounnt
    * deposit amount
    * transfer one account to another account
    * delete account
    * find savings account
    * find current account
    * get debit card
    * get credit card
     */


    /*
     * withdraw amount (accountNumber, withdrawAmount)
     * store both transaction in transaction table;
     * return account with new balance;
     */
    @Override
    public TransactionDTO WithdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber, MinimumAccountBalance, AmountLessThanZero {
        Double Withdraw = null;
        if (withdrawAmount <= 0)
            throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Provide valid amount for withdraw");
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account prevAccount = accountRepo.findById(accountNumber).get();
                Double prevBalance = prevAccount.getAccountInitialBalance();

                if (prevBalance < withdrawAmount) {
                    throw new MinimumAccountBalance("WithdrawAmount is greater than Current Account Balance");
                } else Withdraw = prevBalance - withdrawAmount;

                prevAccount.setAccountInitialBalance(Withdraw);
                //save new balance account;
                accountRepo.save(prevAccount);
                //save transactions for this account
                transactionsRepository.save(saveTransactions(withdrawAmount,prevAccount,"withdraw Amount"));
                //conversion entity to Dto
                return convertTransactionsEntityToDTO(transactions);

            } else throw new InvalidAccountNumber("provide valid account number for withdraw amount");
        }
    }
    /*
    * save transactions for deposit and withdraw;
    * return transaction for save in dao;
     */
    public Transactions saveTransactions(Double withdrawAmount,Account prevAccount,String message){
        transactions.setTransactionAmount(withdrawAmount);
        transactions.setTransactionMessage(message);
        transactions.setToAccountNumber(Integer.toString(prevAccount.getAccountNumber()));
        transactions.setFromAccountNumber("Self");
        transactions.setTransactionId(random.nextInt(50));

        transactions.setTransactionDate(String.valueOf(java.time.LocalDate.now()));
        transactions.setTransactionTime(sdf.format(date));

       return transactions;

    }

    private TransactionDTO convertTransactionsEntityToDTO(Transactions transactions) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactions.getTransactionId());
        transactionDTO.setTransactionDate(transactions.getTransactionDate());
        transactionDTO.setTransactionAmount(transactions.getTransactionAmount());
        transactionDTO.setTransactionMessage(transactions.getTransactionMessage());
        transactionDTO.setTransactionTime(transactions.getTransactionTime());

        return transactionDTO;
    }

    /*
* deposit amount ( accountNumber, depositedAmount)
* store transaction in transaction table;
* return account with new balance;
 */

    @Override
    public TransactionDTO DepositAmount(Integer accountNumber, Double DepositedAmount) throws InvalidAccountNumber, AmountLessThanZero {
        if (DepositedAmount <= 0) {
            throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Provide valid amount for Deposit");
        } else {
            if (accountRepo.existsById(accountNumber)) {
                //account
                Account prevAccount = accountRepo.findById(accountNumber).get();

                Double prevBalance = prevAccount.getAccountInitialBalance() + DepositedAmount;
                prevAccount.setAccountInitialBalance(prevBalance);
                accountRepo.save(prevAccount);
                //transactions
                //save transactions for this account
                transactionsRepository.save(saveTransactions(DepositedAmount,prevAccount,"Deposit amount"));
                //conversion of entity to dto for view;
                return convertTransactionsEntityToDTO(transactions);
            } else throw new InvalidAccountNumber("provide valid account number for deposit amount");
        }
    }
/*
* transfer one account to another account( firstAccountNumber, secondAccountNumber, Amount)
* store both transactions in transaction table;
* return both first and second account with new balances;
 */
    @Override
    public TransactionDTO transferOneToAnother(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance {
        // first account for withdraw
        Account getFirstAccount = accountRepo.findById(firstAccountNumber).get();
        Double balanceInFirstAccount = getFirstAccount.getAccountInitialBalance();
        if (balanceInFirstAccount < amount) {
            throw new MinimumAccountBalance("Do not transfer amount because of less balance in first account");
        } else balanceInFirstAccount = balanceInFirstAccount - amount;

        getFirstAccount.setAccountInitialBalance(balanceInFirstAccount);
        //save first account
        accountRepo.save(getFirstAccount);
        //find second account
        Account getSecondAccount = accountRepo.findById(secondAccountNumber).get();

        Double balanceInSecondAccount = getSecondAccount.getAccountInitialBalance() + amount;
        getSecondAccount.setAccountInitialBalance(balanceInSecondAccount);

        // save second amount
        accountRepo.save(getSecondAccount);
        //transaction credentials
        transactions.setFromAccountNumber(Integer.toString(getFirstAccount.getAccountNumber()));
        transactions.setToAccountNumber(Integer.toString(getSecondAccount.getAccountNumber()));
        transactions.setTransactionMessage("Money Transfer");
        transactions.setTransactionDate(String.valueOf(java.time.LocalDate.now()));
        transactions.setTransactionTime(sdf.format(date));
        transactions.setTransactionAmount(amount);
        transactions.setTransactionId(random.nextInt(50));

        transactionsRepository.save(transactions);

        return convertTransactionsEntityToDTO(transactions);


    }

    @Override
    public List findSavingsAccounts() throws NotPresentAnyAccount {
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> savingAccounts = new ArrayList<>();
        allAccounts.forEach(account -> {
            if (account.getAccountType().equals("Savings")) savingAccounts.add(account);
        });

        if (savingAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have savings accounts");
        else return savingAccounts;
    }

    @Override
    public List findCurrentAccounts() throws NotPresentAnyAccount {
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> currentAccounts = new ArrayList<>();
        allAccounts.forEach(account -> {
            if (account.getAccountType().equals("current")) currentAccounts.add(account);
        });

        if (currentAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have current accounts");
        else return currentAccounts;
    }



    @Override
    public List<String> getDebitCard(Integer accountNumber) throws InvalidAccountNumber {
        List<String> debitCard = new ArrayList<>();
        if (accountRepo.existsById(accountNumber)) {
            debitCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
            debitCard.add("Card Number: 1234 5264 8597");
            debitCard.add("Date: 03/22 To 04/26");
            debitCard.add("CVV: 233");
            debitCard.add("Debit Card");
            return debitCard;
        } else throw new InvalidAccountNumber("Provide valid account number for Debit card");

    }


    @Override
    public List<String> getCreditCard(Integer accountNumber) throws NotEligibleForCreditCard {
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
        } else throw new InvalidAccountNumber("Provide valid account number for Debit card");
    }



    @Override
    public Iterable<Account> GetAllAccount() {
        Iterable<Account> allAccounts = accountRepo.findAll();
        return allAccounts;
    }

    @Override
    public Boolean DeleteAccount(Integer accountNumber) throws InvalidAccountNumber {
        Account account = accountRepo.findById(accountNumber).get();
        if (account.getAccountInitialBalance() > 0) {
            throw new MinimumAccountBalance("Account has some balance, Withdraw balance : " + account.getAccountInitialBalance());
        } else if (account.getAccountInitialBalance() == 0) {
            accountRepo.deleteById(accountNumber);
            // customerRepository.deleteById(accountNumber);
            return true;
        } else throw new InvalidAccountNumber("provide valid account number for delete account");
    }


   // check how many accounts in one id;

    @Override
    public List<Account> findAccountInOneId(Integer customerId) {
        List<Account> accountList = accountRepo.findAllByCustomerId(customerId);
        return accountList;
    }
}





