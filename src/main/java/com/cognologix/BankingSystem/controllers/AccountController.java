package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.AccountService;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.dto.AccountDTO;
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
  * all Accounts
   */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> all(){
      List<Account> all =  accountService.allAccount();
      if (all.isEmpty()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }else return new ResponseEntity<>(all, HttpStatus.OK);
    }
    /*
    * Delete Account
     */
    @DeleteMapping(value = "/delete/{accountNumber}")
    public ResponseEntity<SuccessResponse> deleteAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber {
        SuccessResponse response = accountService.deleteAccount(accountNumber);
        if (response.getSuccess().equals(false)){
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }else return new ResponseEntity<>(response,HttpStatus.OK);
    }
    /*
    * Savings Account
     */
    @GetMapping(value = "/savings")
    public ResponseEntity<List<Account>> savingAccounts() throws AccountsNotExist {
        List<Account> savingsAccounts = accountService.savingsAccounts();
        if (savingsAccounts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else return new ResponseEntity<>(savingsAccounts,HttpStatus.FOUND);
    }
    /*
    * Current Accounts
     */
    @GetMapping(value = "/current")
    public ResponseEntity<List<Account>> currentAccounts() throws AccountsNotExist {
        List<Account> currentAccount = accountService.currentAccounts();
        if (currentAccount.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else return new ResponseEntity<>(currentAccount,HttpStatus.FOUND);
    }
    /*
    * Debit Card
     */
    @GetMapping(value = "/debitCard/{accountNumber}")
    public ResponseEntity<List<String>> debitCard(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        List<String> debitCard = accountService.debitCard(accountNumber);
        return new ResponseEntity<>(debitCard,HttpStatus.CREATED);
    }
    /*
    * Credit Card
     */
    @GetMapping(value = "/creditCard/{accountNumber}")
    public ResponseEntity<List<String>> creditCard(@PathVariable Integer accountNumber) throws NotEligibleForCreditCard {
        List<String> creditCard = accountService.creditCard(accountNumber);
        return new ResponseEntity<>(creditCard,HttpStatus.CREATED);
    }
    /*
    * Both Current and Savings Account
     */
    @GetMapping(value = "/accountsWithSameId/{customerId}")
    public ResponseEntity<List<AccountDTO>> sameId(@PathVariable Integer customerId) throws InvalidCustomerId {
        List<AccountDTO> accountList = accountService.sameId(customerId);
        if (accountList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else return new ResponseEntity<>(accountList,HttpStatus.FOUND);
    }
    /*
    * Delete All Accounts
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        return new ResponseEntity<>(accountService.deleteAll(),HttpStatus.OK);
    }
    /*
    * single Account
     */
    @GetMapping("/singleAccount/{accountNumber}")
    public ResponseEntity<AccountDTO> singleAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        return new ResponseEntity<>(accountService.singleAccount(accountNumber),HttpStatus.FOUND);
    }

}
