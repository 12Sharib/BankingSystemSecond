package com.cognologix.BankingSystem.controllers.Loan;

import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.LoanService;
import com.cognologix.BankingSystem.dto.LoanDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RestController
@RequestMapping("/Loan")
public class LoanController {
    @Autowired
    LoanService loanService;
    
    @GetMapping("/eligible/{creditScore}")
    public ResponseEntity<SuccessResponse> checkEligibility(@PathVariable Integer creditScore){
        log.info("Started with creditScore..");
        SuccessResponse response = loanService.eligible(creditScore);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.OK:HttpStatus.NOT_ACCEPTABLE;
        log.info("completed: " + httpStatus);
        return new ResponseEntity<>(response,httpStatus);

    }
    @PostMapping("/request/{creditScore}/{accountNumber}/{loanType}/{amount}")
    public ResponseEntity<SuccessResponse> requestLoan(@PathVariable Integer creditScore, @PathVariable Integer accountNumber,@PathVariable String loanType, @PathVariable Double amount){
        log.info("Started with creditScore, accountNumber, loanType, amount");
        SuccessResponse response = loanService.request(creditScore,accountNumber,loanType,amount);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.CREATED:HttpStatus.NOT_ACCEPTABLE;
        log.info("Completed: " + httpStatus);
        return new ResponseEntity<>(response,httpStatus);
    }

    @GetMapping("/activate/{loanNumber}")
    public ResponseEntity<LoanDTO> activate(@PathVariable Integer loanNumber){
        log.info("Started with loanNumber..");
        LoanDTO loanDTO = loanService.activate(loanNumber);
        log.info("Completed..");
        return new ResponseEntity<>(loanDTO,HttpStatus.OK);
    }
    @GetMapping("/status/{loanNumber}")
    public ResponseEntity<SuccessResponse> status(@PathVariable Integer loanNumber){
        log.info("Started with loanNumber");
        SuccessResponse response = loanService.status(loanNumber);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.FOUND:HttpStatus.NOT_FOUND;
        log.info("Completed..");
        return new ResponseEntity<>(response,httpStatus);
    }
    @GetMapping("/details/{loanNumber}")
    public ResponseEntity<LoanDTO> details(@PathVariable Integer loanNumber){
        log.info("Started with loanNumber..");
        LoanDTO loanDTO = loanService.details(loanNumber);
        log.info("Completed..");
        return new ResponseEntity<>(loanDTO,HttpStatus.FOUND);
    }

    @PutMapping("/updateDetails/{loanNumber}")
    public ResponseEntity<SuccessResponse> updateDetails(@RequestBody Loan loanDetails, @PathVariable Integer loanNumber){
        log.info("Started with newDetails, loanNumber..");
        SuccessResponse response = loanService.updateDetails(loanDetails,loanNumber);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.CREATED:HttpStatus.NOT_MODIFIED;
        log.info("Completed.." + httpStatus);
        return new ResponseEntity<>(response,httpStatus);
    }
    @DeleteMapping("/delete/{loanNumber}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Integer loanNumber){
        log.info("Started with loanNumber..");
        SuccessResponse response = loanService.delete(loanNumber);
        HttpStatus httpStatus = response.getSuccess().equals(true)?HttpStatus.FOUND:HttpStatus.NOT_FOUND;
        log.info("Completed.." + httpStatus);
        return new ResponseEntity<>(response,httpStatus);
    }
}
