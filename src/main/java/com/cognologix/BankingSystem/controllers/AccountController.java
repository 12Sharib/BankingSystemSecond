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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/account")
@Log4j2
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    AccountService accountService;
  /*
  * all Accounts
   */
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> all(){
        log.info("Accessed All Accounts");
      List<AccountDTO> all =  accountService.allAccount();
      HttpStatus httpStatus = all.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
      return new ResponseEntity<>(all, httpStatus);
    }
    /*
    * Delete Account
     */
    @DeleteMapping(value = "/delete/{accountNumber}")
    public ResponseEntity<SuccessResponse> deleteAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber {
        log.info("In Delete Account With Account Number");
        SuccessResponse response = accountService.deleteAccount(accountNumber);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.OK:HttpStatus.BAD_REQUEST;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(response,httpStatus);
    }
    /*
    * Savings Account
     */
    @GetMapping(value = "/savings")
    public ResponseEntity<List<Account>> savingAccounts() throws AccountsNotExist {
        log.info("Accessed Savings Account");
        List<Account> savingsAccounts = accountService.savingsAccounts();
        HttpStatus httpStatus = savingsAccounts.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(savingsAccounts,httpStatus);
    }
    /*
    * Current Accounts
     */
    @GetMapping(value = "/current")
    public ResponseEntity<List<Account>> currentAccounts() throws AccountsNotExist {
        log.info("Accessed Current Account");
        List<Account> currentAccount = accountService.currentAccounts();
        HttpStatus httpStatus = currentAccount.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(currentAccount,httpStatus);
    }
    /*
    * Debit Card
     */
    @GetMapping(value = "/debitCard/{accountNumber}")
    public ResponseEntity<List<String>> debitCard(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        log.info("Accessed Debit Card With Account Number");
        List<String> debitCard = accountService.debitCard(accountNumber);
        HttpStatus httpStatus = debitCard.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(debitCard,httpStatus);
    }
    /*
    * Credit Card
     */
    @GetMapping(value = "/creditCard/{accountNumber}")
    public ResponseEntity<List<String>> creditCard(@PathVariable Integer accountNumber) throws NotEligibleForCreditCard {
        log.info("Accessed Credit Card With Account Number");
        List<String> creditCard = accountService.creditCard(accountNumber);
        HttpStatus httpStatus = creditCard.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.CREATED;
        log.info("Completed: "+httpStatus);
        return new ResponseEntity<>(creditCard,httpStatus);
    }
    /*
    * Both Current and Savings Account
     */
    @GetMapping(value = "/accountsWithSameId/{customerId}")
    public ResponseEntity<List<AccountDTO>> sameId(@PathVariable Integer customerId) throws InvalidCustomerId {
        log.info("Accessed AccountsWithSameId With Customer Id");
        List<AccountDTO> accountList = accountService.sameId(customerId);
        HttpStatus httpStatus = accountList.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(accountList,httpStatus);
    }
    /*
    * Delete All Accounts
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        log.info("Accessed Delete All");
        HttpStatus httpStatus = accountService.deleteAll().getSuccess().equals(true)?HttpStatus.OK:HttpStatus.NO_CONTENT;
        log.info("Completed: " +httpStatus);
        return new ResponseEntity<>(accountService.deleteAll(),httpStatus);
    }
    /*
    * single Account
     */
    @GetMapping("/singleAccount/{accountNumber}")
    public ResponseEntity<AccountDTO> singleAccount(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        log.info("Accessed Single Account With Account Number");
        AccountDTO accountDTO = accountService.singleAccount(accountNumber);
        HttpStatus httpStatus = accountDTO==null?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(accountDTO,httpStatus);
    }

}
