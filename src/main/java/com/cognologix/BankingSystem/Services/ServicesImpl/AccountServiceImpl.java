package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.convertor.TransactorConvertor;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;


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
    public TransactionDTO withdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber, MinimumAccountBalance, AmountLessThanZero {
        Double withdraw = null;
        if (withdrawAmount <= 0)
            throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Invalid amount for withdraw");
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account prevAccount = accountRepo.findById(accountNumber).get();
                Double prevBalance = prevAccount.getAccountInitialBalance();

                if (prevBalance < withdrawAmount) {
                    throw new MinimumAccountBalance("WithdrawAmount is greater than Current Account Balance");
                } else withdraw = prevBalance - withdrawAmount;

                prevAccount.setAccountInitialBalance(withdraw);
                //save new balance account;
                accountRepo.save(prevAccount);
                //save transactions for this account
                transactionsRepository.save(saveTransactions(withdrawAmount,prevAccount,"withdraw Amount"));
                //conversion entity to Dto
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);

            } else throw new InvalidAccountNumber("Invalid account number for withdraw amount");
        }
    }
    /*
    * save transactions for deposit and withdraw;
    * return transaction for save in dao;
     */
    public Transactions saveTransactions(Double withdrawAmount,Account prevAccount,String message){
        Random random = new Random();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");

        transactions.setTransactionAmount(withdrawAmount);
        transactions.setTransactionMessage(message);
//        transactions.setToAccountNumber(Integer.toString(prevAccount.getAccountNumber()));
//        transactions.setFromAccountNumber("Self");
        transactions.setAccountNumber(prevAccount.getAccountNumber());
        transactions.setTotalBalance(prevAccount.getAccountInitialBalance());
        transactions.setTransactionId(random.nextInt(50));
        transactions.setTransactionDate(String.valueOf(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        transactions.setTransactionTime(sdf.format(date));

       return transactions;

    }

    /*
    * deposit amount ( accountNumber, depositedAmount)
    * store transaction in transaction table;
    * return account with new balance;
     */

    @Override
    public TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber, AmountLessThanZero {
        if (depositedAmount <= 0) {
            //"Amount is less than zero or Equal to Zero, Provide valid amount for Deposit"
            throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Invalid amount for Deposit");
        } else {
            if (accountRepo.existsById(accountNumber)) {
                //account
                Account prevAccount = accountRepo.findById(accountNumber).get();

                Double prevBalance = prevAccount.getAccountInitialBalance() + depositedAmount;
                prevAccount.setAccountInitialBalance(prevBalance);
                accountRepo.save(prevAccount);
                //transactions
                //save transactions for this account
                transactionsRepository.save(saveTransactions(depositedAmount,prevAccount,"Deposit amount"));
                //conversion of entity to dto for view;
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);
            } else throw new InvalidAccountNumber("Invalid account number for deposit amount");
        }
    }
/*
* transfer one account to another account( firstAccountNumber, secondAccountNumber, Amount)
* store both transactions in transaction table;
* return both first and second account with new balances;
 */
    @Override
    public TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance {
        // first account for withdraw
        Account getFirstAccount = accountRepo.findById(firstAccountNumber).get();
        Double balanceInFirstAccount = getFirstAccount.getAccountInitialBalance();
        if (balanceInFirstAccount < amount) {
            throw new MinimumAccountBalance("Invalid balance in first Account, because of less balance in first account");
        } else balanceInFirstAccount = balanceInFirstAccount - amount;

        getFirstAccount.setAccountInitialBalance(balanceInFirstAccount);
        //save first account
        accountRepo.save(getFirstAccount);
        //saved sender transaction
        transactionsRepository.save(saveTransactions(amount,getFirstAccount,"Money Paid, Sent Successfully" + ": To Account:" +secondAccountNumber));

        //find second account
        Account getSecondAccount = accountRepo.findById(secondAccountNumber).get();

        Double balanceInSecondAccount = getSecondAccount.getAccountInitialBalance() + amount;
        getSecondAccount.setAccountInitialBalance(balanceInSecondAccount);

        // save second amount
        accountRepo.save(getSecondAccount);
        //save receiver transaction
        Transactions secondAccountTransactions = saveTransactions(amount,getSecondAccount,"Receive, Received Successfully:" + " from Account:" +firstAccountNumber);
        //transaction credentials
     //   secondAccountTransactions.setFromAccountNumber(Integer.toString(getFirstAccount.getAccountNumber()));
        transactionsRepository.save(secondAccountTransactions);

        //money transfer message
         TransactionDTO transferDTO =  TransactorConvertor.convertTransactionsEntityToDTO(secondAccountTransactions);
         transferDTO.setTransactionMessage("Money Transfer successfully");
         return transferDTO;
    }

    @Override
    public List savingsAccounts() throws NotPresentAnyAccount {
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> savingAccounts = new ArrayList<>();
        allAccounts.forEach(account -> {
            if (account.getAccountType().equalsIgnoreCase("Savings")) savingAccounts.add(account);
        });
        if (savingAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have savings accounts");
        else return savingAccounts;
    }

    @Override
    public List currentAccounts() throws NotPresentAnyAccount {
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> currentAccounts = new ArrayList<>();
        allAccounts.forEach(account -> {
            if (account.getAccountType().equalsIgnoreCase("current")) currentAccounts.add(account);
        });

        if (currentAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have current accounts");
        else return currentAccounts;
    }

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
        } else throw new InvalidAccountNumber("Invalid account number for Debit card");
    }

    @Override
    public List<Account> allAccount() {
        List<Account> allAccounts = accountRepo.findAll();
        return allAccounts;
    }

    @Override
    public Boolean deleteAccount(Integer accountNumber) throws InvalidAccountNumber {
        Account account = accountRepo.findById(accountNumber).get();
        if (account.getAccountInitialBalance() > 0) {
            throw new MinimumAccountBalance("Account has some balance, Withdraw balance : " + account.getAccountInitialBalance());
        } else if (account.getAccountInitialBalance() == 0) {
            accountRepo.deleteById(accountNumber);
            // customerRepository.deleteById(accountNumber);
            return true;
        } else throw new InvalidAccountNumber("Invalid account number for delete account");
    }

   // check how many accounts in one id;
    @Override
    public List<Account> accountsInSameId(Integer customerId) {
        if (accountRepo.existsByCustomerId(customerId)){
            return accountRepo.findAllByCustomerId(customerId);
        }else throw new InvalidAccountNumber("Invalid Customer Id");
    }

    @Override
    public String deleteAll() {
        accountRepo.deleteAll();
        return "Delete Successfully";
    }

    @Override
    public Optional<Account> singleAccount(Integer accountNumber) {
        if (accountRepo.existsById(accountNumber)){
            return accountRepo.findById(accountNumber);

        }else {
            throw new InvalidAccountNumber("Invalid Account Number");
        }
    }
}





