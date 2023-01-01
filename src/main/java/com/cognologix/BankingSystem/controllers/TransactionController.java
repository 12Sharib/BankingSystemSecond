package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.TransactionService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Log4j2
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountRepo accountRepo;
   /*
   * transfer Amount
    */
    @PutMapping(value = "/transferAmount/{firstAccountNumber}/{secondAccountNumber}/{amount}")
    public ResponseEntity<TransactionDTO> transferAmount(@PathVariable Integer firstAccountNumber, @PathVariable Integer secondAccountNumber, @PathVariable Double amount) throws InsufficientBalance, InvalidAccountNumber {
        log.info("Starting transferAmount with firstAccountNumber, secondAccountNumber & amount...");
        TransactionDTO transfer = transactionService.transferAmount(firstAccountNumber, secondAccountNumber, amount);
        HttpStatus httpStatus = transfer ==null?HttpStatus.NOT_MODIFIED:HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(transfer,httpStatus);
    }
    /*
    * Deposit
     */
    @PutMapping(value = "/deposit/{accountNumber}/{depositedAmount}")
    public ResponseEntity<TransactionDTO> depositAmount(@PathVariable Integer accountNumber, @PathVariable Double depositedAmount) throws InvalidAccountNumber, InsufficientBalance {
        log.info("Starting depositAmount with accountNumber & depositedAmount...");
        TransactionDTO Deposit =  transactionService.depositAmount(accountNumber,depositedAmount);
        HttpStatus httpStatus = Deposit==null?HttpStatus.NOT_MODIFIED:HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(Deposit , httpStatus);
    }
    /*
    withdraw
     */
    @PutMapping("/withdraw/{accountNumber}/{withdrawAmount}")
    public ResponseEntity<TransactionDTO> withdrawAmount(@PathVariable Integer accountNumber, @PathVariable Double withdrawAmount) throws InsufficientBalance,InvalidAccountNumber {
        log.info("Starting withdrawAmount with accountNumber & amount...");
        TransactionDTO withdraw =  transactionService.withdrawAmount(accountNumber,withdrawAmount);
        HttpStatus httpStatus = withdraw==null?HttpStatus.NOT_MODIFIED:HttpStatus.CREATED;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(withdraw , httpStatus);
    }

    /*
    * get transaction on one account number;
     */
    @GetMapping("/oneAccountTransactions/{accountNumber}")
    public ResponseEntity<List<Transactions>> oneAccountTransactions(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        log.info("Starting oneAccountTransactions with transactionId...");
        List<Transactions> transactions = transactionService.oneAccountTransactions(accountNumber);
        HttpStatus httpStatus = transactions.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(transactions,httpStatus);
    }
    /*
    * find transaction on trasaction id
     */
    @GetMapping("/findByTransactionId/{transactionId}")
    public ResponseEntity<Transactions> transactionId(@PathVariable Integer transactionId) throws InvalidTransactionId {
        log.info("Starting findByTransactionId with transactionId...");
        Transactions transactions = transactionService.transactionId(transactionId);
        HttpStatus httpStatus = transactions==null?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(transactions,httpStatus);
    }
    /*
    * delete one transaction
     */
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<SuccessResponse> deleteTransaction(@PathVariable Integer transactionId) throws InvalidTransactionId{
        log.info("Starting deleteTransaction with trasactionId...");
        SuccessResponse response = transactionService.deleteTransaction(transactionId);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.OK:HttpStatus.NOT_FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(response,httpStatus);
    }
    /*
    * get transaction by custom date
     */
    @GetMapping("/byDate/{date}")
    public ResponseEntity<List<Transactions>> byDate(@PathVariable String date){
        log.info("Starting byDate with date(as @PathVariable)...");
        List<Transactions> transactionsList = transactionService.byDate(date);
        HttpStatus httpStatus = transactionsList.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(transactionService.byDate(date),httpStatus);
    }
    /*
    * previous five Transactions
     */
    @GetMapping("/previousFiveTransactions/{accountNumber}")
    public ResponseEntity<List<Transactions>> previousFive(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        log.info("Starting previoudFive with accountNumber...");
        List<Transactions> transactionsList = transactionService.previousFive(accountNumber);
        HttpStatus httpStatus = transactionsList.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.FOUND;
        log.info("Completed: " +httpStatus);
        return new ResponseEntity<>(transactionsList,httpStatus);
    }
}
