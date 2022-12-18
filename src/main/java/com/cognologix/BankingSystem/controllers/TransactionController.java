package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.TransactionService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
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
        TransactionDTO transfer = transactionService.transferAmount(firstAccountNumber, secondAccountNumber, amount);
        return new ResponseEntity<>(transfer,HttpStatus.CREATED);
    }
    /*
    * Deposit
     */
    @PutMapping(value = "/deposit/{accountNumber}/{depositedAmount}")
    public ResponseEntity<TransactionDTO> depositAmount(@PathVariable Integer accountNumber, @PathVariable Double depositedAmount) throws InvalidAccountNumber, InsufficientBalance {
        TransactionDTO Deposit =  transactionService.depositAmount(accountNumber,depositedAmount);
        return new ResponseEntity<>(Deposit , HttpStatus.CREATED);
    }
    /*
    withdraw
     */
    @PutMapping("/withdraw/{accountNumber}/{withdrawAmount}")
    public ResponseEntity<TransactionDTO> withdrawAmount(@PathVariable Integer accountNumber, @PathVariable Double withdrawAmount) throws InsufficientBalance,InvalidAccountNumber {
        TransactionDTO withdraw =  transactionService.withdrawAmount(accountNumber,withdrawAmount);
        return new ResponseEntity<>(withdraw , HttpStatus.CREATED);
    }

    /*
    * get transaction on one account number;
     */
    @GetMapping("/oneAccountTransactions/{accountNumber}")
    public ResponseEntity<List<Transactions>> oneAccountTransactions(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        List<Transactions> transactions = transactionService.oneAccountTransactions(accountNumber);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * find transaction on trasaction id
     */
    @GetMapping("/findByTransactionId/{transactionId}")
    public ResponseEntity<Transactions> transactionId(@PathVariable Integer transactionId) throws InvalidTransactionId {
        Transactions transactions = transactionService.transactionId(transactionId);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * delete one transaction
     */
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<SuccessResponse> deleteTransaction(@PathVariable Integer transactionId) throws InvalidTransactionId{
        return new ResponseEntity<>(transactionService.deleteTransaction(transactionId),HttpStatus.OK);
    }
    /*
    * get transaction by custom date
     */
    @GetMapping("/byDate/{date}")
    public ResponseEntity<List<Transactions>> byDate(@PathVariable String date){
        return new ResponseEntity<>(transactionService.byDate(date),HttpStatus.OK);
    }
    /*
    * previous five Transactions
     */
    @GetMapping("/previousFiveTransactions/{accountNumber}")
    public ResponseEntity<List<Transactions>> previousFive(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        return new ResponseEntity<>(transactionService.previousFive(accountNumber),HttpStatus.OK);
    }
}
