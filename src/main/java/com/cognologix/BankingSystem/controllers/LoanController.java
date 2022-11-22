package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.Services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Bank/Loan")
public class LoanController {
    @Autowired
    LoanService loanService;
    @GetMapping("/createLoan/{accountNumber}")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan, @PathVariable Integer accountNumber){
        Loan newLoan = loanService.createLoan(loan,accountNumber);
        return new ResponseEntity<>(newLoan, HttpStatus.OK);

    }
}
