package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.AmountLessThanZero;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.NotEligibleForCreditCard;
import com.cognologix.BankingSystem.Exceptions.MinimumAccountBalance;
import com.cognologix.BankingSystem.Exceptions.NotPresentAnyAccount;
import com.cognologix.BankingSystem.Model.Account;
import org.springframework.web.bind.annotation.*;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Services.ServicesImpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/Bank")
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountServiceImpl accountService;
    @PatchMapping(value = "/transferOneToAnother/{firstAccountNumber}/{secondAccountNumber}/{amount}")
    public ResponseEntity<List<Account>> transferOneToAnother(@PathVariable Integer firstAccountNumber, @PathVariable Integer secondAccountNumber, @PathVariable Double amount) throws MinimumAccountBalance,InvalidAccountNumber {
        if(accountRepo.existsById(firstAccountNumber)){
            if(accountRepo.existsById(secondAccountNumber)){
                List<Account> transfer = accountService.transferOneToAnother(firstAccountNumber, secondAccountNumber, amount);
                return new ResponseEntity<>(transfer,HttpStatus.OK);
            }else throw new InvalidAccountNumber("provide valid second account number");
        } else throw new InvalidAccountNumber("provide valid first account Number");

    }
    @PatchMapping(value = "/deposit/{accountNumber}/{depositedAmount}")
    public ResponseEntity<Account> DepositAmount(@PathVariable Integer accountNumber, @PathVariable Double depositedAmount) throws InvalidAccountNumber,AmountLessThanZero{
        Account Deposit =  accountService.DepositAmount(accountNumber,depositedAmount);

        return new ResponseEntity<>(Deposit , HttpStatus.OK);
    }

    @PatchMapping("/withdraw/{AccountNumber}/{WithdrawAmount}")
    public ResponseEntity<Account> WithdrawAmount(@PathVariable Integer AccountNumber, @PathVariable Double WithdrawAmount) throws AmountLessThanZero,InvalidAccountNumber,MinimumAccountBalance{
        Account Withdraw =  accountService.WithdrawAmount(AccountNumber,WithdrawAmount);
         return new ResponseEntity<>(Withdraw , HttpStatus.OK);
    }

    @GetMapping("/allAccounts")
    public ResponseEntity<Iterable<Account>> GetAllAccounts(){
      Iterable<Account> allAccounts =  accountService.GetAllAccount();
      return new ResponseEntity<>(allAccounts, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/delete/{AccountNumber}")
    public ResponseEntity<Iterable<Account>> DeleteAccount(@PathVariable Integer AccountNumber) throws InvalidAccountNumber {
        accountService.DeleteAccount(AccountNumber);

        return new ResponseEntity<>(accountRepo.findAll(),HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/savings")
    public ResponseEntity<List<Account>> findSavingAccount() throws NotPresentAnyAccount {
        List<Account> savingsAccounts = accountService.findSavingsAccounts();

        return new ResponseEntity<>(savingsAccounts,HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/current")
    public ResponseEntity<List<Account>> findCurrentAccount() throws NotPresentAnyAccount{
        List<Account> currentAccount = accountService.findCurrentAccounts();
        return new ResponseEntity<>(currentAccount,HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/getDebitCard/{accountNumber}")
    public ResponseEntity<List<String>> getDebitCard(@PathVariable Integer accountNumber) throws InvalidAccountNumber{
        List<String> debitCard = accountService.getDebitCard(accountNumber);
        return new ResponseEntity<>(debitCard,HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/getCreditCard/{accountNumber}")
    public ResponseEntity<List<String>> getCreditCard(@PathVariable Integer accountNumber) throws NotEligibleForCreditCard {
        List<String> creditCard = accountService.getCreditCard(accountNumber);
        return new ResponseEntity<>(creditCard,HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/findAccountsInCustomerId/{customerId}")
    public ResponseEntity<List<Account>> findAccountInOneId(@PathVariable Integer customerId){
        List<Account> accountList = accountService.findAccountInOneId(customerId);
        return new ResponseEntity<>(accountList,HttpStatus.OK);
    }

}
