package com.cognologix.BankingSystem.convertor;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.dto.AccountDTO;

public class AccountConvertor {
    public static AccountDTO convertEntityToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO(
                account.getAccountNumber(),
                account.getCustomerId(),
                account.getAccountName(),
                account.getAccountType(),
                account.getAccountInitialBalance(),
                account.getAccountStatus());
        return accountDTO;
    }
}
