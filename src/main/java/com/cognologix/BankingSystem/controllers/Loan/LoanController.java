package com.cognologix.BankingSystem.controllers.Loan;

import com.cognologix.BankingSystem.Model.Loan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/Loan")
public class LoanController {
    
    @GetMapping("checkEligble/{accountNumber}")
    public ResponseEntity checkEligibility(@PathVariable Integer accountNumber){
        return null;
    }
    @PostMapping("/request/{accountNumber}/{loanType}/{amount}")
    public ResponseEntity<?> requestLoan(@PathVariable Integer accountNumber,@PathVariable String loanType,@PathVariable Double amount){
        return null;
    }
    @GetMapping("/status/{loanNumber}")
    public ResponseEntity status(@PathVariable Integer loanNumber){
        return null;
    }
    @GetMapping("/details/{loanNumber}")
    public ResponseEntity details(@PathVariable Integer loanNumber){
        return null;
    }
    @GetMapping("/emis/{loanNumber}")
    public ResponseEntity emis(@PathVariable Integer loanNumber){
        return null;
    }
    @PutMapping("/updateDetails/{loanNumber}")
    public ResponseEntity updateDetails(@RequestBody Loan loanDetails, @PathVariable Integer loanNumber){
        return null;
    }
    @DeleteMapping("/delete/{loanNumber}")
    public ResponseEntity delete(@PathVariable Integer loanNumber){
        return null;
    }
    @PutMapping("/payEmi/{amount}/loanNumber")
    public ResponseEntity payEmi(@PathVariable Double amount, @PathVariable Integer loanNumber){
        return null;
    }
}
