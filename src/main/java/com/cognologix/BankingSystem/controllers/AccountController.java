package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

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
    @GetMapping("/allAccounts")
    public ResponseEntity<List<Account>> all(){
      List<Account> all =  accountService.allAccount();
      return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{accountNumber}")
    public ResponseEntity<SuccessResponse> deleteAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber {
        return new ResponseEntity<>(accountService.deleteAccount(accountNumber),HttpStatus.OK);
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
    @GetMapping(value = "/accountsWithSameId/{customerId}")
    public ResponseEntity<List<Account>> sameId(@PathVariable Integer customerId){
        List<Account> accountList = accountService.sameId(customerId);
        return new ResponseEntity<>(accountList,HttpStatus.OK);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        return new ResponseEntity<>(accountService.deleteAll(),HttpStatus.OK);
    }
    @GetMapping("/singleAccount/{accountNumber}")
    public ResponseEntity<Optional<Account>> singleAccount(@PathVariable Integer accountNumber){
        return new ResponseEntity<>(accountService.singleAccount(accountNumber),HttpStatus.OK);
    }

}
