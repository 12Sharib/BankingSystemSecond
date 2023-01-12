package com.cognologix.BankingSystem.Enums.Error;

import com.cognologix.BankingSystem.Exceptions.InvalidCreditScore;

public enum ErrorCodes {
     INVALID_ACCOUNT_NUMBER(501),
     INVALID_CUSTOMER_ID(502),
    INVALID_TRANSACTION_ID(503),
    INVALID_DOCUMENT(504),
    INVALID_AMOUNT(505),
    INVALID_INPUT(506),
    ACCOUNT_NOT_EXIST(507),
    INSUFFICIENT_BALANCE(508),
    NOT_ELIGIBLE_FOR_CREDITCARD(510),
     INVALID_CREDIT_SCORE(511),

     INVALID_LOAN_NUMBER(512);


     final Integer code;
    ErrorCodes(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

}
