package com.cognologix.BankingSystem.convertor;

import com.cognologix.BankingSystem.Model.Transactions;
import com.cognologix.BankingSystem.dto.TransactionDTO;

public class TransactorConvertor {
    public static TransactionDTO convertTransactionsEntityToDTO(Transactions transactions) {
        TransactionDTO transactionDTO = new TransactionDTO(
                transactions.getTransactionId(),
                transactions.getTransactionDate(),
                transactions.getTransactionAmount(),
                transactions.getTransactionMessage(),
                transactions.getTransactionTime(),
                transactions.getAccountNumber()
        );
        return transactionDTO;
    }
}
