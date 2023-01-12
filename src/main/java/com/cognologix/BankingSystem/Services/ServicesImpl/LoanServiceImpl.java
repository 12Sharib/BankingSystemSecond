package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Enums.Error.ErrorMessages;
import com.cognologix.BankingSystem.Exceptions.InsufficientBalance;
import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCreditScore;
import com.cognologix.BankingSystem.Exceptions.InvalidLoanNumber;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Loan;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.Loan.LoanRepo;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.LoanService;
import com.cognologix.BankingSystem.convertor.LoanConverter;
import com.cognologix.BankingSystem.dto.LoanDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Log4j2
@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private Loan loan;
    @Autowired
    private LoanRepo loanRepo;

    @Override
    public SuccessResponse eligible(Integer creditScore) {
        log.info("Started method...");
        if (creditScore > 0) {
            if (creditScore < 600) {
                return new SuccessResponse("Not Eligible, Less CreditScore", false);
            } else{
                log.info("completed method...");
                return new SuccessResponse("Eligible for Loans", true);
            }
        } else throw new InvalidCreditScore(ErrorMessages.INVALID_CREDIT_SCORE.getErrorMessage() + creditScore);

    }

    @Override
    public SuccessResponse request(Integer creditScore, Integer accountNumber, String loanType, Double amount) {
        log.info("Started method...");
        if (creditScore <= 0) {
            throw new InvalidCreditScore(ErrorMessages.INVALID_CREDIT_SCORE.getErrorMessage() + creditScore);
        } else {
            if (creditScore <= 600) {
                return new SuccessResponse("Not Eligible, Minimum Balance", false);
            } else {
                try {
                    if (accountRepo.existsById(accountNumber)){
                        Loan previousLoan = loanRepo.findByLoanAccountNumber(accountNumber);
                        if (previousLoan.getLoanType().equals(loanType)){
                            return new SuccessResponse("Currently Complete First Loan, Not Eligible",false);
                        }else {
                            log.info("Completed method..");
                            SuccessResponse response = approval(accountNumber, amount, loanType);
                            log.info(response);
                            return response;
                        }
                    }else throw new InvalidAccountNumber(ErrorMessages.INVALID_ACCOUNT_NUMBER.getErrorMessage() + accountNumber);
                } catch (Exception exception) {
                    log.fatal(exception);
                    throw new RuntimeException(exception.getMessage());
                }
            }
        }
    }

    @Override
    public LoanDTO activate(Integer loanNumber) {
        log.info("Started method...");
        if (loanRepo.existsByLoanNumber(loanNumber)) {
            Loan previousLoan = loanRepo.findByLoanNumber(loanNumber);
            log.info("Compelted method...");
            return saveActivationDetails(previousLoan);
        } else throw new InvalidLoanNumber(ErrorMessages.INVALID_LOAN_NUMBER.getErrorMessage() + loanNumber);

    }
    private LoanDTO saveActivationDetails(Loan previousLoan){
        log.info("Saving Details..");
        if (previousLoan.getLoanAmount()<10000){
            previousLoan.setLoanEmiMonths(12);
        }else {
            previousLoan.setLoanEmiMonths(24);
        }
        previousLoan.setLoanStatus("Activated");
        loanRepo.save(previousLoan);
        log.info("details saved..");
        return LoanConverter.convertLoanEntityToDto(previousLoan);
    }

    @Override
    public SuccessResponse status(Integer loanNumber) {
        log.info("Started method...");
        if (loanRepo.existsByLoanNumber(loanNumber)){
           String status = loanRepo.findByLoanNumber(loanNumber).getLoanStatus();
           log.info("Completed method...");
           return new SuccessResponse(status,true);
        }else throw new InvalidLoanNumber(ErrorMessages.INVALID_LOAN_NUMBER.getErrorMessage() + loanNumber);
    }

    @Override
    public LoanDTO details(Integer loanNumber) {
        log.info("Started method...");
        if (loanRepo.existsByLoanNumber(loanNumber)){
            Loan previousLoan = loanRepo.findByLoanNumber(loanNumber);
            log.info("Completed method...");
            return LoanConverter.convertLoanEntityToDto(previousLoan);
        }else throw new InvalidLoanNumber(ErrorMessages.INVALID_LOAN_NUMBER.getErrorMessage() + loanNumber);
    }

    @Override
    public SuccessResponse updateDetails(Loan loanDetails, Integer loanNumber) {
        log.info("Started method...");
        if (loanRepo.existsByLoanNumber(loanNumber)){
            Loan previousLoan = loanRepo.findByLoanNumber(loanNumber);
            previousLoan.setLoanAccountName(loanDetails.getLoanAccountName());
            loanRepo.save(previousLoan);
            log.info("Completed method...");
            return new SuccessResponse("Updated Successfully",true);
        }else throw new InvalidLoanNumber(ErrorMessages.INVALID_LOAN_NUMBER.getErrorMessage() + loanNumber);

    }

    @Override
    public SuccessResponse delete(Integer loanNumber) {
        log.info("Started method..");

        if(loanRepo.existsByLoanNumber(loanNumber)){
            Loan previousLoan = loanRepo.findByLoanNumber(loanNumber);
            if (previousLoan.getLoanAmount() == 0 ){
                loanRepo.delete(previousLoan);
                log.info("Completed method...");
                return new SuccessResponse("Delete Successfully", true);
            }else {
                log.info("Completed method...");
                return new SuccessResponse("Pending Amount, First Cleat that amount",false);
            }

        }else throw new InvalidLoanNumber(ErrorMessages.INVALID_LOAN_NUMBER.getErrorMessage() + loanNumber);

    }

    private SuccessResponse approval(Integer accountNumber,Double amount,String loanType) {
        SuccessResponse response =new SuccessResponse();
        log.info("Started loan approval..");
        Account account = accountRepo.findById(accountNumber).get();
        if (account.getAccountInitialBalance()>1000){
            loan = saveDetails(account,amount,loanType);
            log.info("Completed approval..");
            return new SuccessResponse("Approved with 12% Interest, If agree activate with loanNumber: " + loan.getLoanNumber(),true);
        }else throw new InsufficientBalance(ErrorMessages.INSUFFICIENT_BALANCE.getErrorMessage() + account.getAccountInitialBalance());

    }
    private Loan saveDetails(Account account,Double amount, String loanType){
        log.info("saving details..");
        Random random = new Random();
        loan.setLoanId(random.nextInt(50));
        loan.setLoanNumber(random.nextInt(50));
        loan.setLoanType(loanType);
        loan.setLoanStatus("Deactivated");
        loan.setLoanAccountName(account.getAccountName());
        loan.setLoanAmount(amount);
        loan.setLoanAccountNumber(account.getAccountNumber());

        loanRepo.save(loan);

        log.info("saved..");
        return loan;
    }
}
