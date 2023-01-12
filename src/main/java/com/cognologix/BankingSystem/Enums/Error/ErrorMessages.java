package com.cognologix.BankingSystem.Enums.Error;

public enum ErrorMessages {
    INVALID_ACCOUNT_NUMBER("Invalid Account Number, provide valid accountNumber: "),
    INVALID_CUSTOMER_ID("Invalid customerId, provide valid customerId: "),
    INVALID_TRANSACTION_ID("Invalid transactionId, provide valid transactionId: "),
    INVALID_DOCUMENT("Invalid Document, provide valid document"),
    INVALID_AMOUNT("Invalid amount, provide valid amount: "),
    INVALID_INPUT("Invalid input, provide valid input"),
    ACCOUNT_NOT_EXIST("Empty, Don't Find any account: "),
    INSUFFICIENT_BALANCE("Insufficient balance, take a look on balance: "),
    INVALID_CREDIT_SCORE("Invalid credit score, provide valid score: "),

    NOT_ELIGIBLE_FOR_CREDITCARD("Invalid request, provide minimum balance in account i.e 2000: "),

    INVALID_LOAN_NUMBER("Invalid Loan Number, provide valid loanNumber: ");

    final String errorMessage;
    ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
