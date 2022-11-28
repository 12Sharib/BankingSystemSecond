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
    @GetMapping("/getAllTransaction")
    public ResponseEntity<List<Transactions>> getAllTransaction(){
        List<Transactions> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }

    /*
    * get transaction on one account number;
     */
    @GetMapping("/getOneAccountTransaction/{accountNumber}")
    public ResponseEntity<List<Transactions>> getOneAccountTransaction(@PathVariable Integer accountNumber){
        List<Transactions> transactions = transactionService.getOneAccountTransaction(accountNumber);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * find transaction on trasaction id
     */
    @GetMapping("/findByTransactionId/{transactionId}")
    public ResponseEntity<Optional<Transactions>> findByTransactionId(@PathVariable Integer transactionId){
        Optional<Transactions> transactions = transactionService.findTransactionOnTransactionId(transactionId);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
}
