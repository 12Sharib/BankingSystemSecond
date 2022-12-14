package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidAmount;
import com.cognologix.BankingSystem.Exceptions.InvalidTransactionId;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.TransactionsRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.TransactionService;
import com.cognologix.BankingSystem.convertor.TransactorConvertor;
import com.cognologix.BankingSystem.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private Transactions transactions;
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private AccountRepo accountRepo;

    /*
     * withdraw amount (accountNumber, withdrawAmount)
     * store both transaction in transaction table;
     * return account with new balance;
     */
    @Override
    public TransactionDTO withdrawAmount(Integer accountNumber, Double withdrawAmount) throws InvalidAccountNumber, InsufficientBalance,InvalidAmount {
        Double withdraw = null;
        if (withdrawAmount <= 0)
            throw new InvalidAmount("Amount is less than zero or Equal to Zero, Invalid amount for withdraw");
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account prevAccount = accountRepo.findById(accountNumber).get();
                Double prevBalance = prevAccount.getAccountInitialBalance();

                if (prevBalance < withdrawAmount) {
                    throw new InsufficientBalance("WithdrawAmount is greater than Current Account Balance");
                } else withdraw = prevBalance - withdrawAmount;

                prevAccount.setAccountInitialBalance(withdraw);
                //save new balance account;
                accountRepo.save(prevAccount);
                //save transactions for this account
                transactionsRepository.save(saveTransactions(withdrawAmount,prevAccount,"withdraw Amount"));
                //conversion entity to Dto
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);

            } else throw new InvalidAccountNumber("Invalid account number for withdraw amount");
        }
    }
    /*
     * save transactions for deposit and withdraw;
     * return transaction for save in dao;
     */
    public Transactions saveTransactions(Double withdrawAmount,Account prevAccount,String message){
        Random random = new Random();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");

        transactions.setTransactionAmount(withdrawAmount);
        transactions.setTransactionMessage(message);
        transactions.setAccountNumber(prevAccount.getAccountNumber());
        transactions.setTotalBalance(prevAccount.getAccountInitialBalance());
        transactions.setTransactionId(random.nextInt(50));
        transactions.setTransactionToAccount(0);
        transactions.setTransactionFromAccount(0);
        transactions.setTransactionDate(String.valueOf(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        transactions.setTransactionTime(sdf.format(date));

        return transactions;

    }

    /*
     * deposit amount ( accountNumber, depositedAmount)
     * store transaction in transaction table;
     * return account with new balance;
     */

    @Override
    public TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber, InvalidAmount {
        if (depositedAmount <= 0) {
            //"Amount is less than zero or Equal to Zero, Provide valid amount for Deposit"
            throw new InvalidAmount("Amount is less than zero or Equal to Zero, Invalid amount for Deposit");
        } else {
            if (accountRepo.existsById(accountNumber)) {
                //account
                Account prevAccount = accountRepo.findById(accountNumber).get();

                Double prevBalance = prevAccount.getAccountInitialBalance() + depositedAmount;
                prevAccount.setAccountInitialBalance(prevBalance);
                accountRepo.save(prevAccount);
                //transactions
                //save transactions for this account
                transactionsRepository.save(saveTransactions(depositedAmount,prevAccount,"Deposit amount"));
                //conversion of entity to dto for view;
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);
            } else throw new InvalidAccountNumber("Invalid account number for deposit amount");
        }
    }
    /*
     * transfer one account to another account( firstAccountNumber, secondAccountNumber, Amount)
     * store both transactions in transaction table;
     * return both first and second account with new balances;
     */
    @Override
    public TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws InsufficientBalance,InvalidAmount,InvalidAccountNumber {
        // first account for withdraw
        if(amount<=0){
            throw new InvalidAmount("Invalid Amount, provide valid amount");
        }else {
            Account getFirstAccount = accountRepo.findById(firstAccountNumber).get();
            Double balanceInFirstAccount = getFirstAccount.getAccountInitialBalance();
            if (balanceInFirstAccount < amount) {
                throw new InsufficientBalance("Invalid balance, because of less balance in first account");
            } else balanceInFirstAccount = balanceInFirstAccount - amount;

            getFirstAccount.setAccountInitialBalance(balanceInFirstAccount);
            //save first account
            accountRepo.save(getFirstAccount);
            //saved sender transaction
            Transactions firstAccountTransactions = saveTransactions(amount, getFirstAccount, "Money Paid, Sent Successfully");
            firstAccountTransactions.setTransactionToAccount(secondAccountNumber);
            firstAccountTransactions.setTransactionFromAccount(firstAccountNumber);
            transactionsRepository.save(firstAccountTransactions);

            //find second account
            Account getSecondAccount = accountRepo.findById(secondAccountNumber).get();

            Double balanceInSecondAccount = getSecondAccount.getAccountInitialBalance() + amount;
            getSecondAccount.setAccountInitialBalance(balanceInSecondAccount);

            // save second amount
            accountRepo.save(getSecondAccount);
            //save receiver transaction
            Transactions secondAccountTransactions = saveTransactions(amount, getSecondAccount, "Receive, Received Successfully");
            //transaction credentials
            secondAccountTransactions.setTransactionFromAccount(firstAccountNumber);
            transactionsRepository.save(secondAccountTransactions);

            //money transfer message
            TransactionDTO transferDTO = TransactorConvertor.convertTransactionsEntityToDTO(secondAccountTransactions);
            transferDTO.setTransactionMessage("Money Transfer successfully");
            return transferDTO;
        }
    }


    @Override
    public List<Transactions> oneAccountTransactions(Integer accountNumber) throws InvalidAccountNumber {
        if (transactionsRepository.existsByAccountNumber(accountNumber)) {
            List<Transactions> transactionsList = transactionsRepository.findByAccountNumber(accountNumber);
            return transactionsList;
        }else throw new InvalidAccountNumber("Invalid Account Number for Single Account Transactions");

    }

    @Override
    public Transactions transactionId(Integer transactionId) throws InvalidTransactionId{
        if (transactionsRepository.existsById(transactionId)) {
            return transactionsRepository.findById(transactionId).get();
        }else throw new InvalidTransactionId("Invalid TransactionId");
    }

    @Override
    public SuccessResponse deleteTransaction(Integer transactionId) throws InvalidTransactionId{
        String message = null;
        if(transactionsRepository.existsById(transactionId)){
            transactionsRepository.deleteById(transactionId);
            return new SuccessResponse("Delete Successfully",true);
        }else throw new InvalidTransactionId("Invalid transactionId");
    }

    @Override
    public List byDate(String date) {
        List<Transactions> transactionsList = transactionsRepository.findByTransactionDate(date);
        return transactionsList;
    }

    @Override
    public List previousFive(Integer accountNumber) throws InvalidAccountNumber{
        List<Transactions> previousFive = null;
        if(transactionsRepository.existsByAccountNumber(accountNumber)){
           previousFive = transactionsRepository.previousFiveTransactions(accountNumber);
        }else throw new InvalidAccountNumber("Invalid Account Number for previous Transactions");

        return previousFive;
    }

}
