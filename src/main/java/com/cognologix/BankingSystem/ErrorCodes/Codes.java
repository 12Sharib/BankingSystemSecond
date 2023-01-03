package com.cognologix.BankingSystem.ErrorCodes;

public enum Codes {
     INVALID_ACCOUNT_NUMBER("501"),
     INVALID_CUSTOMER_ID("502"),
    INVALID_TRANSACTION_ID("503");
     final String code;
    Codes(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

}
