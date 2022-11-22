package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.*;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.AccountService;
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

/*
    Withdraw Amount
*/
    @Override
    public Account WithdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber,MinimumAccountBalance,AmountLessThanZero {
        Double Withdraw = null;
        if(withdrawAmount<=0) throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Provide valid amount for withdraw");
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account initialAccount = accountRepo.findById(accountNumber).get();
                Customer intitialCustomer = customerRepository.findById(accountNumber).get();

                Double initialBalance = initialAccount.getAccountInitialBalance();

                if (initialBalance < withdrawAmount) {
                    throw new MinimumAccountBalance("WithdrawAmount is greater than Current Account Balance");
                } else Withdraw = initialBalance - withdrawAmount;

                initialAccount.setAccountInitialBalance(Withdraw);
                intitialCustomer.setCustomerAccountBalance(Withdraw);

                customerRepository.save(intitialCustomer);
                accountRepo.save(initialAccount);
                return accountRepo.findById(accountNumber).get();
            } else throw new InvalidAccountNumber("provide valid account number for withdraw amount");
        }
    }
/*
    Deposit Amount;
 */
    @Override
    public Account DepositAmount(Integer accountNumber, Double DepositedAmount) throws InvalidAccountNumber,AmountLessThanZero{
        Double deposit = null;
        if (DepositedAmount<=0){
            throw new AmountLessThanZero("Amount is less than zero or Equal to Zero, Provide valid amount for Deposit");
        }
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account initialAccount = accountRepo.findById(accountNumber).get();
                Customer initialCustomer = customerRepository.findById(accountNumber).get();

                Double initialBalance = initialAccount.getAccountInitialBalance();
                deposit = initialBalance + DepositedAmount;

                initialCustomer.setCustomerAccountBalance(deposit);
                initialAccount.setAccountInitialBalance(deposit);

                customerRepository.save(initialCustomer);
                accountRepo.save(initialAccount);
                return accountRepo.findById(accountNumber).get();
            } else throw new InvalidAccountNumber("provide valid account number for deposit amount");
        }
    }
/*
    get all accounts in the data base;
 */
    @Override
    public Iterable<Account> GetAllAccount() {
        Iterable<Account> allAccounts = accountRepo.findAll();
        return allAccounts;
    }
/*
    Delete Account in the data base ;
 */
    @Override
    public Boolean DeleteAccount(Integer accountNumber) throws InvalidAccountNumber {
      Account account = accountRepo.findById(accountNumber).get();
        if(account.getAccountInitialBalance() > 0) {
            throw new MinimumAccountBalance("Account has some balance, Withdraw balance : " + account.getAccountInitialBalance());
        } else if (account.getAccountInitialBalance() == 0) {
            accountRepo.deleteById(accountNumber);
            customerRepository.deleteById(accountNumber);
            return true;
        } else throw new InvalidAccountNumber("provide valid account number for delete account");
    }
/*
    Transfer from one account to another account
 */
    @Override
    public List transferOneToAnother(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws MinimumAccountBalance {
        Account getFirstAccount = accountRepo.findById(firstAccountNumber).get();
        Customer getFirstCustomer = customerRepository.findById(firstAccountNumber).get();

        Double balanceInFirstAccount = getFirstAccount.getAccountInitialBalance();
        if(balanceInFirstAccount<amount){
            throw new MinimumAccountBalance("Do not transfer amount because of less balance in first account");
        }else balanceInFirstAccount = balanceInFirstAccount - amount;

        getFirstAccount.setAccountInitialBalance(balanceInFirstAccount);
        getFirstCustomer.setCustomerAccountBalance(balanceInFirstAccount);
        accountRepo.save(getFirstAccount);
        customerRepository.save(getFirstCustomer);

        Account getSecondAccount = accountRepo.findById(secondAccountNumber).get();
        Customer getSecondCustomer = customerRepository.findById(secondAccountNumber).get();

        Double balanceInSecondAccount = getSecondAccount.getAccountInitialBalance() + amount;
        getSecondAccount.setAccountInitialBalance(balanceInSecondAccount);
        getSecondCustomer.setCustomerAccountBalance(balanceInSecondAccount);
        accountRepo.save(getSecondAccount);
        customerRepository.save(getSecondCustomer);

        Account firstAccountForView = accountRepo.findById(firstAccountNumber).get();
        Account secondAccountForView = accountRepo.findById(secondAccountNumber).get();
        List<Account> viewAccount = new ArrayList<>();
        viewAccount.add(firstAccountForView);
        viewAccount.add(secondAccountForView);
        return viewAccount;

      //  return ("Balance in first account: " + balanceInFirstAccount + "\nTotal balance in second account: " +balanceInSecondAccount);
    }
/*
    find all saving accounts;
 */
    @Override
    public List findSavingsAccounts() throws NotPresentAnyAccount {
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> savingAccounts = new ArrayList<>();
        allAccounts.forEach(account -> { if (account.getAccountType().equals("Savings"))  savingAccounts.add(account); });

        if (savingAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have savings accounts");
        else return savingAccounts;
    }
/*
    find all current accounts
 */
    @Override
    public List findCurrentAccounts() throws NotPresentAnyAccount{
        Iterable<Account> allAccounts = accountRepo.findAll();
        List<Account> currentAccounts = new ArrayList<>();
        allAccounts.forEach(account -> { if (account.getAccountType().equals("current"))  currentAccounts.add(account); });

        if(currentAccounts.isEmpty()) throw new NotPresentAnyAccount("Does not have current accounts");
        else return currentAccounts;
    }
/*
    get debit card
 */
    @Override
    public List<String> getDebitCard(Integer accountNumber)  throws InvalidAccountNumber{
        List<String> debitCard = new ArrayList<>();
        if(accountRepo.existsById(accountNumber)){
            debitCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
            debitCard.add("Card Number: 1234 5264 8597");
            debitCard.add("Date: 03/22 To 04/26");
            debitCard.add("CVV: 233");
            debitCard.add("Debit Card");
            return debitCard;
        }else throw new InvalidAccountNumber("Provide valid account number for Debit card");

    }
/*
    get credit card
 */
    @Override
    public List<String> getCreditCard(Integer accountNumber) throws NotEligibleForCreditCard {
        List<String> creditCard = new ArrayList<>();
        if(accountRepo.existsById(accountNumber)){
            if(accountRepo.findById(accountNumber).get().getAccountInitialBalance() > 2000) {
                creditCard.add("Name: " + accountRepo.findById(accountNumber).get().getAccountName());
                creditCard.add("Card Number: 1934 5244 8597");
                creditCard.add("Date: 03/22 To 04/27");
                creditCard.add("CVV: 202");
                creditCard.add("Credit Card");
                return creditCard;
            }else throw new NotEligibleForCreditCard("Balance Less than 2000, Not Eligible for Credit Card");
        }else throw new InvalidAccountNumber("Provide valid account number for Debit card");
    }
/*
    check how many accounts in one id;
 */
    @Override
    public List<Account> findAccountInOneId(Integer customerId) {
        List<Account> accountList = accountRepo.findAllByAccountCustomerId(customerId);

        return accountList;
    }
}

