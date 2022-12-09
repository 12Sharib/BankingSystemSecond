package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    AccountService accountService;
    /*
     * Account Controller
     * transfer one account to another account
     * deposit Money
     * withdraw Money
     * get all account
     * delete account
     * find savings account
     * find current accounts
     * get debit card
     * get credit card
     */
    @PutMapping(value = "/transferAmount/{firstAccountNumber}/{secondAccountNumber}/{amount}")
    public ResponseEntity<TransactionDTO> transferAmount(@PathVariable Integer firstAccountNumber, @PathVariable Integer secondAccountNumber, @PathVariable Double amount) throws MinimumAccountBalance,InvalidAccountNumber {
        if(accountRepo.existsById(firstAccountNumber)){
            if(accountRepo.existsById(secondAccountNumber)){
                TransactionDTO transfer = accountService.transferAmount(firstAccountNumber, secondAccountNumber, amount);
                return new ResponseEntity<>(transfer,HttpStatus.OK);
            }else throw new InvalidAccountNumber("provide valid second account number");
        } else throw new InvalidAccountNumber("provide valid first account Number");

    }
    @PutMapping(value = "/deposit/{accountNumber}/{depositedAmount}")
    public ResponseEntity<TransactionDTO> depositAmount(@PathVariable Integer accountNumber, @PathVariable Double depositedAmount) throws InvalidAccountNumber,AmountLessThanZero{
        TransactionDTO Deposit =  accountService.depositAmount(accountNumber,depositedAmount);
        return new ResponseEntity<>(Deposit , HttpStatus.OK);
    }

    @PutMapping("/withdraw/{accountNumber}/{withdrawAmount}")
    public ResponseEntity<TransactionDTO> withdrawAmount(@PathVariable Integer accountNumber, @PathVariable Double withdrawAmount) throws AmountLessThanZero,InvalidAccountNumber,MinimumAccountBalance{
        TransactionDTO withdraw =  accountService.withdrawAmount(accountNumber,withdrawAmount);
         return new ResponseEntity<>(withdraw , HttpStatus.OK);
    }

    @GetMapping("/allAccounts")
    public ResponseEntity<List<Account>> all(){
      List<Account> all =  accountService.allAccount();
      return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{accountNumber}")
    public ResponseEntity<Iterable<Account>> deleteAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber {
        accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>(accountRepo.findAll(),HttpStatus.OK);
    }
    @GetMapping(value = "/savings")
    public ResponseEntity<List<Account>> savingAccounts() throws NotPresentAnyAccount {
        List<Account> savingsAccounts = accountService.savingsAccounts();
        return new ResponseEntity<>(savingsAccounts,HttpStatus.OK);
    }
    @GetMapping(value = "/current")
    public ResponseEntity<List<Account>> currentAccounts() throws NotPresentAnyAccount{
        List<Account> currentAccount = accountService.currentAccounts();
        return new ResponseEntity<>(currentAccount,HttpStatus.OK);
    }
    @GetMapping(value = "/debitCard/{accountNumber}")
    public ResponseEntity<List<String>> debitCard(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        List<String> debitCard = accountService.debitCard(accountNumber);
        return new ResponseEntity<>(debitCard,HttpStatus.OK);
    }
    @GetMapping(value = "/creditCard/{accountNumber}")
    public ResponseEntity<List<String>> creditCard(@PathVariable Integer accountNumber) throws NotEligibleForCreditCard {
        List<String> creditCard = accountService.creditCard(accountNumber);
        return new ResponseEntity<>(creditCard,HttpStatus.OK);
    }
    @GetMapping(value = "/findAccountsInCustomerId/{customerId}")
    public ResponseEntity<List<Account>> accountsInSameId(@PathVariable Integer customerId){
        List<Account> accountList = accountService.accountsInSameId(customerId);
        return new ResponseEntity<>(accountList,HttpStatus.OK);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll(){
        return new ResponseEntity<>(accountService.deleteAll(),HttpStatus.OK);
    }
    @GetMapping("/singleAccount/{accountNumber}")
    public ResponseEntity<Optional<Account>> singleAccount(@PathVariable Integer accountNumber){
        return new ResponseEntity<>(accountService.singleAccount(accountNumber),HttpStatus.OK);
    }

}
