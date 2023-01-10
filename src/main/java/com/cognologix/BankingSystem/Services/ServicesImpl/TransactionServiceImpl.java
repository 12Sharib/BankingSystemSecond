package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Enums.Error.ErrorMessages;
import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
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
        log.info("Started method..");
        Double withdraw = null;
        if (withdrawAmount <= 0) {
            log.error("Invalid Amount: " + withdraw);
            throw new InvalidAmount(ErrorMessages.INVALID_AMOUNT.getErrorMessage() + withdrawAmount);
        }
        else {
            if (accountRepo.existsById(accountNumber)) {
                Account prevAccount = accountRepo.findById(accountNumber).get();
                Double prevBalance = prevAccount.getAccountInitialBalance();

                if (prevBalance < withdrawAmount) {
                    log.error("Insufficient balance in first account: " + prevBalance);
                    throw new InsufficientBalance(ErrorMessages.INSUFFICIENT_BALANCE.getErrorMessage() + prevBalance);
                } else withdraw = prevBalance - withdrawAmount;

                prevAccount.setAccountInitialBalance(withdraw);
                //save new balance account;
                accountRepo.save(prevAccount);
                //save transactions for this account
                transactionsRepository.save(saveTransactions(withdrawAmount,prevAccount,"withdraw Amount"));
                //conversion entity to Dto
                log.info("Completed method..");
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);

            } else
                log.error("Invalid account Number: " + accountNumber);
                throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() +accountNumber);
        }
    }
    /*
     * save transactions for deposit and withdraw;
     * return transaction for save in dao;
     */
    public Transactions saveTransactions(Double withdrawAmount,Account prevAccount,String message){
        log.info("Creating transaction..");
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
        log.info("Completing transaction..");
        return transactions;

    }

    /*
     * deposit amount ( accountNumber, depositedAmount)
     * store transaction in transaction table;
     * return account with new balance;
     */

    @Override
    public TransactionDTO depositAmount(Integer accountNumber, Double depositedAmount) throws InvalidAccountNumber, InvalidAmount {
        log.info("Started method..");
        if (depositedAmount <= 0) {
            log.error("Invalid amount: " +depositedAmount);
            throw new InvalidAmount(ErrorMessages.INVALID_AMOUNT.getErrorMessage()  + depositedAmount);
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
                log.info("Completed method..");
                return TransactorConvertor.convertTransactionsEntityToDTO(transactions);
            } else
                log.error("Invalid account number: " + accountNumber);
                throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
        }
    }
    /*
     * transfer one account to another account( firstAccountNumber, secondAccountNumber, Amount)
     * store both transactions in transaction table;
     * return both first and second account with new balances;
     */
    @Override
    public TransactionDTO transferAmount(Integer firstAccountNumber, Integer secondAccountNumber, Double amount) throws InsufficientBalance,InvalidAmount,InvalidAccountNumber {
        //check accounts
        log.info("Started method..");
        if(accountRepo.existsById(firstAccountNumber)){
            if(accountRepo.existsById(secondAccountNumber)) {
                if (amount <= 0) {
                    log.error("Invalid Amount: " + amount);
                    throw new InvalidAmount(ErrorMessages.INVALID_AMOUNT.getErrorMessage());
                } else {
                    Account getFirstAccount = accountRepo.findById(firstAccountNumber).get();
                    Double balanceInFirstAccount = getFirstAccount.getAccountInitialBalance();
                    if (balanceInFirstAccount < amount) {
                        log.error("InSufficient Balance for Transfer: " + balanceInFirstAccount);
                        throw new InsufficientBalance(ErrorMessages.INSUFFICIENT_BALANCE.getErrorMessage() + balanceInFirstAccount);
                    } else balanceInFirstAccount = balanceInFirstAccount - amount;

                    getFirstAccount.setAccountInitialBalance(balanceInFirstAccount);
                    //save first account
                    accountRepo.save(getFirstAccount);
                    //saved sender transaction
                    Transactions firstAccountTransactions = saveTransactions(amount, getFirstAccount, "Money Paid, Sent Successfully");
                    firstAccountTransactions.setTransactionToAccount(secondAccountNumber);
                    firstAccountTransactions.setTransactionFromAccount(firstAccountNumber);

                    log.info("Saved first transaction..");
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
                    log.info("Saved second transaction..");
                    transactionsRepository.save(secondAccountTransactions);

                    //money transfer message
                    TransactionDTO transferDTO = TransactorConvertor.convertTransactionsEntityToDTO(secondAccountTransactions);
                    transferDTO.setTransactionMessage("Money Transfer successfully");
                    log.info("Completed method..");
                    return transferDTO;
                }
            }else
                log.error("Invalid Second Account Number: " + secondAccountNumber);
                throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + secondAccountNumber);
        } else
            log.error("Invalid First Account Number: " + firstAccountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + firstAccountNumber);

    }


    @Override
    public List<Transactions> oneAccountTransactions(Integer accountNumber) throws InvalidAccountNumber {
        log.info("Started method..");
        if (transactionsRepository.existsByAccountNumber(accountNumber)) {
            List<Transactions> transactionsList = transactionsRepository.findByAccountNumber(accountNumber);
            log.info("Completed method..");
            return transactionsList;
        }else
            log.error("Invalid Account Number: " + accountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);

    }

    @Override
    public Transactions transactionId(Integer transactionId) throws InvalidTransactionId{
        log.info("Started method");
        if (transactionsRepository.existsById(transactionId)) {
            log.info("Completed method..");
            return transactionsRepository.findById(transactionId).get();
        }else
            log.error("Invalid Transaction Id: " + transactionId);
            throw new InvalidTransactionId(ErrorMessages.INVALID_TRANSACTION_ID.getErrorMessage() + transactionId);
    }

    @Override
    public SuccessResponse deleteTransaction(Integer transactionId) throws InvalidTransactionId{
        log.info("Started method..");
        String message = null;
        if(transactionsRepository.existsById(transactionId)){
            transactionsRepository.deleteById(transactionId);
            log.info("Completed method..");
            return new SuccessResponse("Delete Successfully",true);
        }else
            log.error("Invalid transactionId: " + transactionId);
            throw new InvalidTransactionId(ErrorMessages.INVALID_TRANSACTION_ID.getErrorMessage() + transactionId);
    }

    @Override
    public List byDate(String date) {
        log.info("Started method..");
        List<Transactions> transactionsList = transactionsRepository.findByTransactionDate(date);
        if (transactionsList.isEmpty()){
            throw new AccountsNotExist(ErrorMessages.ACCOUNT_NOT_EXIST.getErrorMessage());
        }else {
            log.info("Completed method..");
            return transactionsList;
        }
    }

    @Override
    public List previousFive(Integer accountNumber) throws InvalidAccountNumber{
        log.info("Started method..");
        List<Transactions> previousFive = null;
        if(transactionsRepository.existsByAccountNumber(accountNumber)){
           previousFive = transactionsRepository.previousFiveTransactions(accountNumber);
        }else {
            log.error("Invalid Account Number: " + accountNumber);
            throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
        }
        log.info("Completed method..");
        return previousFive;
    }
}
