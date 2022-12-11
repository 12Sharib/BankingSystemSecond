package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.TransactionService;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping(value = "/transferAmount/{firstAccountNumber}/{secondAccountNumber}/{amount}")
    public ResponseEntity<TransactionDTO> transferAmount(@PathVariable Integer firstAccountNumber, @PathVariable Integer secondAccountNumber, @PathVariable Double amount) throws MinimumAccountBalance, InvalidAccountNumber {
        if(accountRepo.existsById(firstAccountNumber)){
            if(accountRepo.existsById(secondAccountNumber)){
                TransactionDTO transfer = transactionService.transferAmount(firstAccountNumber, secondAccountNumber, amount);
                return new ResponseEntity<>(transfer,HttpStatus.OK);
            }else throw new InvalidAccountNumber("provide valid second account number");
        } else throw new InvalidAccountNumber("provide valid first account Number");

    }
    @PutMapping(value = "/deposit/{accountNumber}/{depositedAmount}")
    public ResponseEntity<TransactionDTO> depositAmount(@PathVariable Integer accountNumber, @PathVariable Double depositedAmount) throws InvalidAccountNumber, AmountLessThanZero {
        TransactionDTO Deposit =  transactionService.depositAmount(accountNumber,depositedAmount);
        return new ResponseEntity<>(Deposit , HttpStatus.OK);
    }

    @PutMapping("/withdraw/{accountNumber}/{withdrawAmount}")
    public ResponseEntity<TransactionDTO> withdrawAmount(@PathVariable Integer accountNumber, @PathVariable Double withdrawAmount) throws AmountLessThanZero,InvalidAccountNumber,MinimumAccountBalance{
        TransactionDTO withdraw =  transactionService.withdrawAmount(accountNumber,withdrawAmount);
        return new ResponseEntity<>(withdraw , HttpStatus.OK);
    }
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
    public ResponseEntity<Transactions> transactionId(@PathVariable Integer transactionId){
        Transactions transactions = transactionService.transactionId(transactionId);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    /*
    * delete one transaction
     */
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<SuccessResponse> deleteTransaction(@PathVariable Integer transactionId){
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
    public ResponseEntity<List<Transactions>> previousFive(@PathVariable Integer accountNumber){
        return new ResponseEntity<>(transactionService.previousFive(accountNumber),HttpStatus.OK);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<SuccessResponse> deleteAll(){
        return new ResponseEntity<>(transactionService.deleteAll(),HttpStatus.OK);
    }

}
