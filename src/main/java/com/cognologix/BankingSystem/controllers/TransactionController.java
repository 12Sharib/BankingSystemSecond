package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountRepo accountRepo;
    /*
    * get all transactions;
    * get how many transactions in one account
     */


    /*
    * get all transactions
     */
    @GetMapping("/allTransaction")
    public ResponseEntity<List<Transactions>> all(){
        List<Transactions> transactions = transactionService.all();
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }

    /*
    * get transaction on one account number;
     */
    @GetMapping("/oneAccountTransactions/{accountNumber}")
    public ResponseEntity<List<Transactions>> oneAccountTransactions(@PathVariable Integer accountNumber){
        List<Transactions> transactions = transactionService.oneAccountTransactions(accountNumber);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * find transaction on trasaction id
     */
    @GetMapping("/findByTransactionId/{transactionId}")
    public ResponseEntity<Transactions> findByTransactionId(@PathVariable Integer transactionId){
        Transactions transactions = transactionService.findTransactionOnTransactionId(transactionId);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * delete one transaction
     */
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<String> deleteOneTransaction(@PathVariable Integer transactionId){
        return new ResponseEntity<>(transactionService.deleteOneTransaction(transactionId),HttpStatus.OK);
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
    public ResponseEntity<List<Transactions>> previousFive(@PathVariable Integer accountNumber){
        return new ResponseEntity<>(transactionService.previousFive(accountNumber),HttpStatus.OK);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll(){
        return new ResponseEntity<>(transactionService.deleteAll(),HttpStatus.OK);
    }

}
